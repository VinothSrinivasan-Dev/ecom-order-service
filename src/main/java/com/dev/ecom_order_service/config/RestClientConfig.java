package com.dev.ecom_order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // 1 Way of Creating RestClient
    @Bean
     public RestClient restClient(){
      return RestClient.create();
     }
}
