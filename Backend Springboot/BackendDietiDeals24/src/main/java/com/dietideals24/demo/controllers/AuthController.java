package com.dietideals24.demo.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dietideals24.demo.models.dto.TokenDto;
import com.dietideals24.demo.models.dto.UrlDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@RestController
public class AuthController {

	@Value("${spring.security.oauth2.resourceserver.opaque-token.clientId}")
	private String clientId;
	@Value("${spring.security.oauth2.resourceserver.opaque-token.clientSecret}")
	private String clientSecret;
	
	
	@GetMapping("/auth/url")
	public ResponseEntity<UrlDto> auth(){
		String url = new GoogleAuthorizationCodeRequestUrl(
				clientId,
				"http://localhost:5432",
				Arrays.asList("email","profile","openid")
				).build();
		return ResponseEntity.ok(new UrlDto(url));
	}
	
	 @GetMapping("/auth/callback")
	    public ResponseEntity<TokenDto> callback(@RequestParam("code") String code) throws URISyntaxException {

	        String token;
	        try {
	            token = new GoogleAuthorizationCodeTokenRequest(
	                    new NetHttpTransport(), new GsonFactory(),
	                    clientId,
	                    clientSecret,
	                    code,
	                    "http://localhost:5432"
	            ).execute().getAccessToken();
	        } catch (IOException e) {
	            System.err.println(e.getMessage());
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        return ResponseEntity.ok(new TokenDto(token));
	    }
}
