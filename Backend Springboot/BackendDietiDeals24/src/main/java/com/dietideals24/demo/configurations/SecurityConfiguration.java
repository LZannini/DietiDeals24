package com.dietideals24.demo.configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ComponentScan
public class SecurityConfiguration {

	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().anyRequest().permitAll(); 
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	                .authorizeRequests()
	                .requestMatchers("/**").permitAll() // Consenti l'accesso a tutte le risorse
	                .anyRequest().permitAll() // Consenti l'accesso a tutte le altre richieste
	                .and()
	            .csrf().disable() // Disabilita CSRF per semplificare la comunicazione da Postman
	            .headers().frameOptions().disable();

	        return http.build();
	    }
}