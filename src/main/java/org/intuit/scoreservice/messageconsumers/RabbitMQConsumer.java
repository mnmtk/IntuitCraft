package org.intuit.scoreservice.messageconsumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.intuit.scoreservice.exceptions.PlayerNotFoundException;
import org.intuit.scoreservice.models.entity.Score;
import org.intuit.scoreservice.services.ScoreService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class RabbitMQConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScoreService scoreService;

    @RabbitListener(queues = "leaderboard", ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            Score scoreObject = objectMapper.readValue(message, Score.class);
            log.info("Consumed score: " + scoreObject);
            scoreService.submitScore(scoreObject);
            channel.basicAck(tag, false); // Manually acknowledge the message
        } catch (PlayerNotFoundException e) {
            log.error("Bad message publised in queue " + e.getMessage());
            channel.basicAck(tag, false); // just delete the message we wont process it.
        } catch (Exception e) {
            log.error("Error processing message: " + e.getMessage());
            try {
                channel.basicNack(tag, false, true); // Negatively acknowledge and requeue the message
            } catch (IOException ioException) {
                log.error("Error sending nack: " + ioException.getMessage());
            }
        }
    }

}
