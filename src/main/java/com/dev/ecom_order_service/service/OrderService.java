package com.dev.ecom_order_service.service;

import com.dev.ecom_order_service.Exceptions.MyCustomRuntimeException;
import com.dev.ecom_order_service.client.InventoryClient;
import com.dev.ecom_order_service.model.Inventory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class OrderService {

    private final RestTemplate restTemplate;
    private final RestClient restClient;
    private final InventoryClient inventoryClient;

    public OrderService(RestTemplate restTemplate, RestClient restClient, InventoryClient inventoryClient) {
        this.restTemplate = restTemplate;
        this.restClient = restClient;
        this.inventoryClient = inventoryClient;
    }
    public String placeOrder(Long productId){
        //TODO Call inventory service to check stock

        Inventory inventory= inventoryClient.getInventory(productId);
        int quantity=inventory.getQuantity();
        updateInventory(inventory);

        return  quantity>0?
                "order Placed Successfully!":
                "Product Out of Stock";
    }

    private void updateInventory(Inventory inventory) {
        inventory.setQuantity(inventory.getQuantity()-1);

       inventoryClient.updateInventory(inventory);

    }
}
