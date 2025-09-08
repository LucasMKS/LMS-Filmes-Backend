// package com.lucasm.lmsfilmes.service;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.amqp.rabbit.annotation.RabbitListener;
// import org.springframework.stereotype.Service;

// import com.lucasm.lmsfilmes.config.RabbitMQConfig;

// @Service
// public class RabbitMQConsumer {

//     private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

//     @RabbitListener(queues = RabbitMQConfig.MOVIE_QUEUE)
//     public void consume(String message) {
//         logger.info("Mensagem recebida do RabbitMQ: {}", message);

//         // Aqui você pode processar a mensagem
//         // Ex: enviar e-mail, atualizar relatório, registrar no banco, etc.
//     }
// }
