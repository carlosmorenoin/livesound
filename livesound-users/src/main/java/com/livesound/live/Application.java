package com.livesound.live;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class Application {



    public static void main(final String[] args) {
        System.setProperty("spring.config.name", "live-users");
        SpringApplication.run(Application.class);
    }
}
