package com.livesound.live.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;

@Configuration
public class MongoConfiguration {

	@Value("${live.database.host}")
	private String host;

	@Bean
	public MongoClient createConnection() {
		return new MongoClient(host);
	}
}
