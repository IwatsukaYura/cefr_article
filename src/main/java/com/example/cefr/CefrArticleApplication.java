package com.example.cefr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CefrArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CefrArticleApplication.class, args);
    }
}
