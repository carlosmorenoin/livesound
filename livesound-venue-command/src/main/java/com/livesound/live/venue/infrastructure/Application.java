package com.livesound.live.venue.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		System.setProperty("spring.config.name", "live-venues-command");
		SpringApplication.run(Application.class);
	}
}
