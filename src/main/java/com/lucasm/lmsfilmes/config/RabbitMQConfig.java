package com.lucasm.lmsfilmes.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MOVIE_QUEUE = "movieQueue";

    @Bean
    public Queue movieQueue() {
        return new Queue(MOVIE_QUEUE, true); // true = fila durável
    }
}
