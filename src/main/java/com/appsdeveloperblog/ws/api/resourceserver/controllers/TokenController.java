package com.appsdeveloperblog.ws.api.resourceserver.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@Slf4j
public class TokenController {
	
	@GetMapping
	public Jwt getToken(@AuthenticationPrincipal Jwt jwt) {
		log.info("token called");
		return jwt;
	}

}
