package com.dev.ecom_order_service.client.config;

import com.dev.ecom_order_service.model.Inventory;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CustomInventoryEncoder implements Encoder {

    private final ObjectMapper objectMapper= new ObjectMapper();



    @Override
    public void encode(Object o, Type type, RequestTemplate requestTemplate) throws EncodeException {

        try{
            if(o instanceof Inventory inventory){

                Map<String,Object> map = new HashMap<>();
                map.put("productId",inventory.getProductId());
                map.put("quantity",inventory.getQuantity());

                String json = objectMapper.writeValueAsString(map);

                requestTemplate.body(json);
                requestTemplate.header("Content-Type","application/json");
            }

        }catch(Exception e){
            throw new RuntimeException("Encoding failure",e);
        }
    }
}
