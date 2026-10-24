package com.dev.ecom_order_service.service;

import com.dev.ecom_order_service.model.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AsyncOrderService {
    private final RabbitTemplate rabbitTemplate;

    public AsyncOrderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public String placeOrder(Long productId) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                1L,101L,420L,2
        );
        rabbitTemplate.convertAndSend(
                "order.exchange",
                "order.created",
                orderCreatedEvent
        );
        return " Async Order Placed Successfully";
    }
}
