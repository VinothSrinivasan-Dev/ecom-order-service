package com.dev.ecom_order_service.model;

public record OrderCreatedEvent(
        Long orderid,
        Long customerId,
        Long productId,
        int quantity
) {
}
