package com.dietideals24.demo.controllers;

import java.security.Principal;
import java.util.List;


import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.dietideals24.demo.models.*;
import com.dietideals24.demo.repository.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

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
