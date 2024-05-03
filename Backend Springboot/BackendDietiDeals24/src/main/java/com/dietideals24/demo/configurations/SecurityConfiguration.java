package com.dietideals24.demo.configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	 @Bean
	    public InMemoryUserDetailsManager userDetailsService() {
	        UserDetails user = User.withDefaultPasswordEncoder()
	                .username("user")
	                .password("password")
	                .roles("COMPRATORE")
	                .build();
	        return new InMemoryUserDetailsManager(user);
	    }
 
	@Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
         http
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults());
        
        return http.build();    
        }
	
}