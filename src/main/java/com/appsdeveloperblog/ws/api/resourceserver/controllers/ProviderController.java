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
@RequestMapping("/prv")
@Slf4j
public class ProviderController {

    @Autowired
    Environment env;

	@GetMapping("/login")
	@PermitAll
	public String login() {
		return getReturnString("anonynous allowed login");
	}

	@GetMapping("/inquireProvider")
	@Secured({"ROLE_gwt_int_si_prv_read","ROLE_gwt_int_si_prv_write","ROLE_gwt_int_si_prv_admin"})
	public String inquire() {
		return getReturnString("inquireProvider");
	}

	@GetMapping("/createProvider")
	@Secured({"ROLE_gwt_int_si_prv_write","ROLE_gwt_int_si_prv_admin"})
	public String create() {
		return getReturnString("createProvider");
	}

	@GetMapping("/updateProvider")
	@Secured({"ROLE_gwt_int_si_prv_write","ROLE_gwt_int_si_prv_admin"})
	public String update() {
		return getReturnString("updateProvider");
	}

	@GetMapping("/editFilterFile")
	@Secured({"ROLE_gwt_int_si_prv_admin"})
	public String editFilterFile() {
		return getReturnString("editFilterFile");
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
