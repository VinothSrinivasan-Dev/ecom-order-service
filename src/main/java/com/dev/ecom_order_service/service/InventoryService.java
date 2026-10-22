package com.dev.ecom_order_service.service;

import com.dev.ecom_order_service.client.InventoryClient;
import com.dev.ecom_order_service.model.Inventory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class InventoryService {

    private final InventoryClient inventoryClient;

    public InventoryService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    /*@Retryable(
            retryFor = RuntimeException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @RateLimiter(
          name="inventoryService",
            fallbackMethod = "fallBackMethod"
    )
    @CircuitBreaker(
            name="inventoryCircuitBreaker",
            fallbackMethod = "circuitBreakerFallbackMethod"
    ) */
    @TimeLimiter(
            name= "inventoryTimeLimiter",
            fallbackMethod = "timeLimiterFallback"
    )
    public CompletableFuture<Inventory> getInventory(Long productId){
        System.out.println("Calling Inventory Service for ProductId: "+productId);
        return CompletableFuture.supplyAsync(()->
            inventoryClient.getInventory(productId)
        );
    }

    public CompletableFuture<Inventory> timeLimiterFallback(Long productId){
        System.out.println("Fallback method called for productId :" +productId);
        Inventory inventory= new Inventory(productId,0);
        return CompletableFuture.completedFuture(inventory);
    }
}
