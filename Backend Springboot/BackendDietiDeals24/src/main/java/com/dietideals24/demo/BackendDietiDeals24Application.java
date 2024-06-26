package com.dietideals24.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan
@EnableScheduling
public class BackendDietiDeals24Application {

	public static void main(String[] args) {
		SpringApplication.run(BackendDietiDeals24Application.class, args);
	}
}		

