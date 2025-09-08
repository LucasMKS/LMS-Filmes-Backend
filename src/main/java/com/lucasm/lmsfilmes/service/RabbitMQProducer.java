// package com.lucasm.lmsfilmes.service;

// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.stereotype.Service;

// import com.lucasm.lmsfilmes.config.RabbitMQConfig;

// @Service
// public class RabbitMQProducer {

//     private final RabbitTemplate rabbitTemplate;

//     public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
//         this.rabbitTemplate = rabbitTemplate;
//     }

//     public void sendMessage(String message) {
//         rabbitTemplate.convertAndSend(RabbitMQConfig.MOVIE_QUEUE, message);
//     }
// }
