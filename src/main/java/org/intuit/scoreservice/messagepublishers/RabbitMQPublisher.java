package org.intuit.scoreservice.messagepublishers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intuit.scoreservice.exceptions.ScorePublishingException;
import org.intuit.scoreservice.models.entity.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Component
@Lazy
@AllArgsConstructor
@Slf4j
public class RabbitMQPublisher implements MessageQueuePublisher {
    private static final String queueName = "leaderboard";
    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ConnectionFactory connectionFactory;

    @Override
    public CompletableFuture<Boolean> publish (Score score) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Publishing score...");
            try (Connection connection = connectionFactory.newConnection();
                 Channel channel = connection.createChannel()) {
                String message = objectMapper.writeValueAsString(score);
                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                        .contentType("application/json")
                        .deliveryMode(2) // Persistent message
                        .build();
                channel.basicPublish("", queueName, properties, message.getBytes(StandardCharsets.UTF_8));
                log.info("Published message: " + message);
                return true;
            } catch (Exception e) {
                log.error("Error publishing message: " + e.getMessage());
                throw new ScorePublishingException("Failed to publish message", e);
            }
        });
    }
}
