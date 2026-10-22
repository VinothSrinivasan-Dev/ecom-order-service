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
import java.util.concurrent.ExecutionException;

@Service
public class OrderService {

    private final RestTemplate restTemplate;
    private final RestClient restClient;
    private final InventoryService inventoryService;
    private final InventoryClient inventoryClient;

    public OrderService(RestTemplate restTemplate, RestClient restClient, InventoryService inventoryService, InventoryClient inventoryClient) {
        this.restTemplate = restTemplate;
        this.restClient = restClient;
        this.inventoryService = inventoryService;
        this.inventoryClient = inventoryClient;
    }
    public String placeOrder(Long productId) throws ExecutionException, InterruptedException {
        //TODO Call inventory service to check stock
//        String response = restTemplate.getForObject(
//                "http://localhost:8083/inventory/"+productId,
//                String.class);
//        ResponseEntity<Inventory> entity=restClient.get()
//                .uri("http://localhost:8083/inventory/"+productId)
//                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError,((request, response) -> {
//                    throw new MyCustomRuntimeException(response.getStatusCode(),response.getHeaders());
//                }))
//                .toEntity(Inventory.class);

      /* Object exchange= restClient.get()
                .uri("http://localhost:8083/inventory/"+productId)
                        .exchange(((clientRequest, clientResponse) ->{
                            if(clientResponse.getStatusCode().is4xxClientError()){
                                 throw new MyCustomRuntimeException(clientResponse.getStatusCode(),clientResponse.getHeaders());
                            }else{
                                return clientResponse.getBody();
                            }
                        } )); */


        Inventory inventory= inventoryService.getInventory(productId).get();
        int quantity=inventory.getQuantity();
        updateInventory(inventory);

        return  quantity>0?
                "order Placed Successfully!":
                "Product Out of Stock";
    }

    private void updateInventory(Inventory inventory) {
       if(inventory.getQuantity() <=0){
           return;
       }
        inventory.setQuantity(inventory.getQuantity()-1);
//        restClient.post()
//                .uri("http://localhost:8083/inventory")
//                .body(inventory)
//                .retrieve()
//                .toBodilessEntity();
       inventoryClient.updateInventory(inventory);

    }
}
