package com.example.testswimmy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestSwimmyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestSwimmyApplication.class, args);
    }

}
