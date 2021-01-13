package com.goldcard.purchase;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.goldcard.purchase.mapper")
public class PurchaseDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PurchaseDemoApplication.class, args);
    }

}
