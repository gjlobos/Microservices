package com.facturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class FacturacionApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacturacionApplication.class, args);
    }

}
