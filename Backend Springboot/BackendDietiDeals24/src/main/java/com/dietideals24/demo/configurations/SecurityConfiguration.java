package com.dietideals24.demo.configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan
public class SecurityConfiguration {

	public void configure(HttpSecurity http) throws Exception {
		http.httpBasic()
	    .and()
	    .authorizeRequests()
	    .requestMatchers("/api/home")
	    .hasRole("ADMIN")
	    .requestMatchers("/api/product/*")
	    .hasRole("ADMIN")
	    .and()
	    .formLogin();
    }
}