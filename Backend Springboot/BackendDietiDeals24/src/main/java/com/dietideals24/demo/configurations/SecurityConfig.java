package com.dietideals24.demo.configurations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtAuthenticationEntryPoint unauthorizedHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserDetailsService userDetailsService;
	
	public SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler,
	                      JwtAuthenticationFilter jwtAuthenticationFilter,
	                      UserDetailsService userDetailsService) {
	    this.unauthorizedHandler = unauthorizedHandler;
	    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	    this.userDetailsService = userDetailsService;
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/auth/**", "/utente/registra", "/oauth2/**").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2Login()
            .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .and()
            .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
        .and()
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
	    
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	    
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	            .userDetailsService(userDetailsService)
	            .passwordEncoder(passwordEncoder())
	            .and()
	            .build();
	}
}
	

