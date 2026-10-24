package com.dev.ecom_order_service.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE = "order.exchange";
    public static final String QUEUE = "inventory.queue";
    public static final String ROUTING_KEY = "order.created";

    private final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Bean
    public ApplicationRunner checkRabbitAdmin(AmqpAdmin amqpAdmin,
                                              ConnectionFactory connectionFactory) {
        return args -> {
            try (Connection c = connectionFactory.createConnection()) {
                System.out.println("RabbitMQ connected: " + c.isOpen());
            }
            amqpAdmin.initialize(); // declares exchange, queue, binding now
        };
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue inventoryQueue() {
        log.info("Creating RabbitMQ binding with routing key: {}",
                ROUTING_KEY);
        return new Queue(QUEUE);

    }

    @Bean
    public Binding inventoryBinding(Queue inventoryQueue, DirectExchange directExchange) {
        log.info("Creating RabbitMQ binding with routing key: {}",
                ROUTING_KEY);
        return BindingBuilder
                .bind(inventoryQueue)
                .to(directExchange)
                .with(ROUTING_KEY);

    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }


}
