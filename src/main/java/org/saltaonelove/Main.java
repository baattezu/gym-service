package org.saltaonelove;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
//        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
//        builder.sources(JpaConfig.class)
//                .run(args);
    }
}