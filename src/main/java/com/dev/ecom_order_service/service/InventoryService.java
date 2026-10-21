package com.dev.ecom_order_service.service;

import com.dev.ecom_order_service.client.InventoryClient;
import com.dev.ecom_order_service.model.Inventory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryClient inventoryClient;

    public InventoryService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @Retryable(
            retryFor = RuntimeException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public Inventory getInventory(Long productId){
        System.out.println("Calling Inventory Service for ProductId: "+productId);
        return inventoryClient.getInventory(productId);
    }
}
