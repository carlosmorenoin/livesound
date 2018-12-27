package com.livesound.live.venue.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "live-venues-query");
		SpringApplication.run(Application.class);
	}
}
