package com.dev.ecom_order_service.service;

import com.dev.ecom_order_service.client.InventoryClient;
import com.dev.ecom_order_service.model.Inventory;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
    )*/
    @Bulkhead(
            name ="inventoryServiceBulkHead",
            type= Bulkhead.Type.SEMAPHORE,
            fallbackMethod = "bullHeadFallbackMethodSP"
    )
    public Inventory getInventorySP(Long productId){
        System.out.println("Calling Inventory Service for ProductId: "+productId);
        return inventoryClient.getInventory(productId);
    }

    public Inventory bullHeadFallbackMethodSP(Long productId,Throwable throwable){
        System.out.println("Fallback method called for productId :" +productId
        +" | Reason: "
                +throwable.getClass().getSimpleName()
        );
        return new Inventory(productId,0);
    }
    @Bulkhead(
            name ="inventoryThreadPool",
            type= Bulkhead.Type.THREADPOOL,
            fallbackMethod = "bullHeadFallbackMethod"
    )
    public CompletableFuture<Inventory> getInventory(Long productId){
        System.out.println("Calling Inventory Service for ProductId: "+productId);
       return  CompletableFuture.supplyAsync(()->
                        inventoryClient.getInventory(productId)
                );
    }

    public CompletableFuture<Inventory> bullHeadFallbackMethod(Long productId){
        System.out.println("Fallback method called for productId :" +productId
                +" | Thread: "
                +Thread.currentThread().getName()
        );
        Inventory inventory = new Inventory(productId,0);
        return CompletableFuture.completedFuture(inventory);
    }
}
