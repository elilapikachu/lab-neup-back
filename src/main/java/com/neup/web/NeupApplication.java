package com.neup.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class, // por defecto sprenboot usa el localhost en mongo, esto es para evitarlo
        MongoDataAutoConfiguration.class
})
public class NeupApplication {
    public static void main(String[] args) {
        SpringApplication.run(NeupApplication.class, args);
    }
}
