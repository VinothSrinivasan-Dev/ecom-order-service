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
//        String response = restTemplate.getForObject(
//                "http://localhost:8083/inventory/"+productId,
//                String.class);
        ResponseEntity<Inventory> entity=restClient.get()
                .uri("http://localhost:8083/inventory/"+productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,((request, response) -> {
                    throw new MyCustomRuntimeException(response.getStatusCode(),response.getHeaders());
                }))
                .toEntity(Inventory.class);

      /* Object exchange= restClient.get()
                .uri("http://localhost:8083/inventory/"+productId)
                        .exchange(((clientRequest, clientResponse) ->{
                            if(clientResponse.getStatusCode().is4xxClientError()){
                                 throw new MyCustomRuntimeException(clientResponse.getStatusCode(),clientResponse.getHeaders());
                            }else{
                                return clientResponse.getBody();
                            }
                        } )); */


//        Inventory inventory= inventoryClient.getInventory(productId);
//        int quantity=inventory.getQuantity();
        updateInventory(entity.getBody());

        return  entity.getBody() !=null && entity.getBody().getQuantity()>0?
                "order Placed Successfully!":
                "Product Out of Stock";
    }

    private void updateInventory(Inventory inventory) {
        inventory.setQuantity(inventory.getQuantity()-1);
        restClient.post()
                .uri("http://localhost:8083/inventory")
                .body(inventory)
                .retrieve()
                .toBodilessEntity();
//       inventoryClient.updateInventory(inventory);

    }
}
