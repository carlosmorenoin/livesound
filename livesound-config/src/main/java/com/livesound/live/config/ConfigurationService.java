package com.livesound.live.config;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableConfigServer
@SpringBootApplication
public class ConfigurationService {

	public static void main(String[] args) {
		SpringApplication.run(ConfigurationService.class, args);
	}
}
