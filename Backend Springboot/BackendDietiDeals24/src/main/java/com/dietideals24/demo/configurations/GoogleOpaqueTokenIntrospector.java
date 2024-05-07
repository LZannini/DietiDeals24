package com.dietideals24.demo.configurations;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import com.dietideals24.demo.models.dto.UserInfo;


public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

	private final WebClient userInfoClient;
	
	
	public GoogleOpaqueTokenIntrospector(WebClient userInfoClient) {
		this.userInfoClient = userInfoClient;
	}


	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		UserInfo userInfo = userInfoClient.get()
		          .uri(uriBuilder -> uriBuilder.path("/oauth/v3/userinfo").queryParam("access_token",token).build())
		          .retrieve()
		          .bodyToMono(UserInfo.class)
		          .block();
		
	Map<String, Object> attributes = new HashMap<>();
	attributes.put("sub", userInfo.sub());
	attributes.put("name", userInfo.name());
	return new OAuth2IntrospectionAuthenticatedPrincipal(userInfo.name(),attributes,null);
		
	}

    

}
