package com.dietideals24.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.dietideals24.demo")
public class BackendDietiDeals24Application {

	public static void main(String[] args) {
		SpringApplication.run(BackendDietiDeals24Application.class, args);
	}

}
