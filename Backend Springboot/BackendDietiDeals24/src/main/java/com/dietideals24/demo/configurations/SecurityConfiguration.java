package com.dietideals24.demo.configurations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	private final WebClient userInfoClient;
	
	public SecurityConfiguration(WebClient userInfoClient) {
		this.userInfoClient = userInfoClient;
	}
	
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
                .cors(Customizer.withDefaults())
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/","/auth/**","/api/**").permitAll()               	
                .anyRequest().authenticated())
                .oauth2ResourceServer(c -> c.opaqueToken(Customizer.withDefaults()));
        
        return http.build();    
        }
	
	 @Bean
	    public OpaqueTokenIntrospector introspector() {
	        return new GoogleOpaqueTokenIntrospector(userInfoClient);
	    }
	
}