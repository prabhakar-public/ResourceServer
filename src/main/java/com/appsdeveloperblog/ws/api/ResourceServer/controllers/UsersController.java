package com.appsdeveloperblog.ws.api.ResourceServer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    Environment env;

	@GetMapping("/status/check")
	public String status() {
		return getReturnString("status check");
	}

	@GetMapping("/status/check2")
	@Secured("ROLE_developer")
	public String status2() {
		return getReturnString("status check2");
	}


	@GetMapping("/status/checkAnon")
	@Secured("ROLE_ANONYMOUS")
	public String anonStatus() {
		return getReturnString("anon status check");
	}

	@GetMapping("/status/checkFree")
	@PermitAll
	public String freeStatus() {
		return getReturnString("free status check");
	}

	private String getReturnString(String prefix) {
		System.out.println(prefix + " called");
		return prefix + " Working... at time = " + LocalDateTime.now();
	}


	@PreAuthorize("hasRole('developer') or #id == #jwt.subject")
    //@Secured("ROLE_developer")
    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return "Deleted user with id " + id + " and JWT subject " + jwt.getSubject();
    }


//    @PostAuthorize("returnObject.userId == #jwt.subject")
//    @GetMapping(path = "/{id}")
//    public UserRest getUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
//        return new UserRest("Sergey", "Kargopolov","5f3fb480-f86c-4514-8d23-ca88d66c6253");
//    }

}
