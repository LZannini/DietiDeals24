package Controller;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
   
	@Bean 
	public PasswordEncoder passwordEncoder() { 
		      
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		      
		        return encoder;
	}
	
	/*@Bean
    *public InMemoryUserDetailsManager userDetailsService() {
		 *
	*	UserDetails user1;
	*	try {
	*		user1 = UserDetails.withUsername("user1").password(passwordEncoder().encode("user1Pass")).roles("USER").build();
	*	} catch (Exception e) {
	*		// TODO Auto-generated catch block
	*		e.printStackTrace();
	*	}
	*
		return new InMemoryUserDetailsManager(user1);
    }
*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	        http.csrf().disable().authorizeRequests().requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/anonymous*").anonymous()
    	            .requestMatchers("/login*")
    	            .permitAll()
    	            .anyRequest()
    	            .authenticated()
    	            .and()
    	            .formLogin()
    	            .loginPage("/login.html")
    	            .loginProcessingUrl("/perform_login")
    	            .defaultSuccessUrl("/homepage.html", true)
    	            .failureUrl("/login.html?error=true")
    	            .failureHandler(authenticationFailureHandler())
    	            .and()
    	            .logout()
    	            .logoutUrl("/perform_logout")
    	            .deleteCookies("JSESSIONID")
    	            .logoutSuccessHandler((LogoutSuccessHandler) logoutSuccessHandler());
    	            return http.build();
    	    }

	private AuthenticationFailureHandler authenticationFailureHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object logoutSuccessHandler() {
		// TODO Auto-generated method stub
		return null;
	}

    
}
