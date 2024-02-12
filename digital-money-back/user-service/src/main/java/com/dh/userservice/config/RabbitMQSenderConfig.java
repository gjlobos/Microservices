package com.dh.userservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQSenderConfig {

    @Value("${queue.user.name}")
    private String userQueue;

    @Bean
    public Queue getUserQueue() {
        return new Queue(this.userQueue, true);
    }

}
