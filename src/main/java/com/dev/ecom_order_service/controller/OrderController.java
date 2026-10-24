package com.dev.ecom_order_service.controller;

import com.dev.ecom_order_service.service.AsyncOrderService;
import com.dev.ecom_order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/order")
public class OrderController {


    private final OrderService orderService;
    private final AsyncOrderService asyncOrderService;
    @Autowired
    public OrderController(OrderService orderService, AsyncOrderService asyncOrderService) {
        this.orderService = orderService;
        this.asyncOrderService = asyncOrderService;
    }

    @PostMapping("/{productId}")
    public String placeOrder(@PathVariable Long productId) throws ExecutionException, InterruptedException {

       return orderService.placeOrder(productId);

    }

    @PostMapping("/async/{productId}")
    public String placeOrderAsync(@PathVariable Long productId) throws ExecutionException, InterruptedException {

        return asyncOrderService.placeOrder(productId);

    }
}
