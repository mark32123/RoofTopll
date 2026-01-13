package com.ll.rooftopll;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ll.rooftopll.mapper")
public class RoofTopllApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoofTopllApplication.class, args);
    }
}
