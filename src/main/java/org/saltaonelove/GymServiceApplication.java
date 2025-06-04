package org.saltaonelove;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GymServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymServiceApplication.class, args);
    }

}