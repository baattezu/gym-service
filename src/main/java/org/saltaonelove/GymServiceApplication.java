package org.saltaonelove;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.common.errors.SerializationException;
import org.saltaonelove.gymshared.util.ExceptionCodeMapper;
import org.saltaonelove.gymshared.util.PermittedEndpoints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@SpringBootApplication
@EnableDiscoveryClient
public class GymServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        addNewExceptions();
        addPermittedEndpoints();
    }

    private void addNewExceptions(){
        ExceptionCodeMapper.addExceptionMapping(SerializationException.class, HttpStatus.NOT_ACCEPTABLE);
    }

    private void addPermittedEndpoints(){
        PermittedEndpoints.addEndpoint(HttpMethod.POST, "/api/trainer");
        PermittedEndpoints.addEndpoint(HttpMethod.POST, "/api/trainee");
        PermittedEndpoints.addEndpoint(HttpMethod.POST, "/api/auth/login");
    }
}