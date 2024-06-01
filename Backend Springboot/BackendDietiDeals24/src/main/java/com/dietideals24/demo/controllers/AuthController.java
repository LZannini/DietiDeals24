package com.dietideals24.demo.controllers;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class AuthController {
	
	
	@GetMapping("/google")
    public String welcomeGoogle() {
    	return "Welcome to Google!!";
    }
	
	 @GetMapping("/facebook")
	    public String welcomeFacebook() {
	        return "Welcome to Facebook!!";
	    }
    
	
    @GetMapping("/user")
    public Principal user(Principal principal) {
    	System.out.println("username: " + principal.getName());
    	return principal;
    }
	
    
    
	
}
