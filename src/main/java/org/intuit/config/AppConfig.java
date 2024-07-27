package org.intuit.config;

import com.rabbitmq.client.ConnectionFactory;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AppConfig {

    private static String leaderBoardQueueName = "leaderboard";

//    @Value("${app.rabbitmq.uri}")
//    private String rabbitMQUri;


    @Bean
    @Qualifier("RedisCommands")
    public RedisCommands<String, String> redisCommands() {
        RedisClient redisClient = RedisClient.create("redis://localhost:6379");
        StatefulRedisConnection connection = redisClient.connect();
        return connection.sync();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public ConnectionFactory rabbitMQConnectionFactory() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@localhost:5672");
        return factory;
    }

//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer(RabbitListenerEndpointRegistry registry) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConcurrentConsumers(5); // Set the minimum number of concurrent consumers
//        container.setMaxConcurrentConsumers(10); // Set the maximum number of concurrent consumers
//        container.setQueueNames("leaderboard"); // Set the queue name
//        container.setMessageListener(new MessageListenerAdapter(new RabbitMQConsumer())); // Set the listener
//        return container;
//    }
}

