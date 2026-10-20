package com.dev.ecom_order_service.Exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        if(response.status() == 404) {
            return new RuntimeException("Product not found");
        }
        return new RuntimeException("Generic Error : "+ response.status());
    }
}
