package com.example.mortgage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
public class MortgageApplication {
	public static void main(String[] args) {
		SpringApplication.run(MortgageApplication.class, args);
	}
}
