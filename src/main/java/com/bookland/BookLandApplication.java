package com.bookland;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bookland.dao")
public class BookLandApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookLandApplication.class, args);
    }

}
