package ru.ntwz.feedify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FeedifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedifyApplication.class, args);
    }

}
