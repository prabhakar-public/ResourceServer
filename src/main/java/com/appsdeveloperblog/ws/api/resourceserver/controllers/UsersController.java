package com.appsdeveloperblog.ws.api.resourceserver.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
@Slf4j
public class UsersController {

    @Autowired
    Environment env;

	@GetMapping("/login")
	@PermitAll
	public String login() {
		return getReturnString("anonynous allowed login");
	}

	@GetMapping("/status/checkFree")
	@PermitAll
	public String freeStatus() {
		return getReturnString("status check free");
	}

	@GetMapping("/status/checkBasic")
	public String statusBasic() {
		return getReturnString("status check basic");
	}

	@GetMapping("/status/checkDev")
	@Secured("ROLE_developer")
	public String status() {
		return getReturnString("status check developer");
	}

	@GetMapping("/status/checkDev2")
	@Secured("ROLE_developer2")
	public String status2() {
		return getReturnString("status check developer2");
	}


	@GetMapping("/status/checkAnon")
	@Secured("ROLE_ANONYMOUS")
	public String anonStatus() {
		return getReturnString("status check anonymous");
	}


	private String getReturnString(String prefix) {
		log.info(prefix + " called");
		return prefix + " Working... at time = " + LocalDateTime.now();
	}


	@PreAuthorize("hasRole('developer') or #id == #jwt.subject")
    //@Secured("ROLE_developer")
    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return "Deleted user with id " + id + " and JWT subject " + jwt.getSubject();
    }

	@GetMapping("/status/getToken")
	public Jwt getToken(@AuthenticationPrincipal Jwt jwt) {
		log.info("token called");
		return jwt;
	}

//    @PostAuthorize("returnObject.userId == #jwt.subject")
//    @GetMapping(path = "/{id}")
//    public UserRest getUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
//        return new UserRest("Sergey", "Kargopolov","5f3fb480-f86c-4514-8d23-ca88d66c6253");
//    }

}
