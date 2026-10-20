package com.dev.ecom_order_service.client.config;

import com.dev.ecom_order_service.Exceptions.CustomErrorDecoder;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class InventoryFeignClientConfig {

    @Bean
    public Logger.Level feignInvLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options options(){
        return new Request.Options(
               Duration.ofMillis(3000),
               Duration.ofMillis(5000),true
        );
    }

    @Bean
    public Retryer  feignRetryer(){
        return new Retryer.Default(
          1L,2L,3

        );
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor(){
        return requestTemplate -> {
            requestTemplate.header("X-Correlation-Id", UUID.randomUUID().toString());
        };

    }
    @Bean
    public ErrorDecoder feignErrorDecoder(){
        return new CustomErrorDecoder();
    }

    @Bean
    public Encoder feignEncoder(){
        return new CustomInventoryEncoder();
    }

//    @Bean
//    public Decoder feignDecoder(){
//        return new CustomErrorDecoder()
//    }
}
