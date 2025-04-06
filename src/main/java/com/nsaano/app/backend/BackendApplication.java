package com.nsaano.app.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		 // Load environment variables from .env file
		 EnvLoader.loadEnv();
		SpringApplication.run(BackendApplication.class, args);
	}

} 
