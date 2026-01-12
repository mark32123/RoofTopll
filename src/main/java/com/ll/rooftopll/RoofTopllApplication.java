package com.ll.rooftopll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RoofTopllApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoofTopllApplication.class, args);
    }
}
