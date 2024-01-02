/**
 * 
 */
package com.hospital.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.User;
import com.hospital.service.interfaces.AuthorityService;
import com.hospital.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * 
 */
@RestController
@RequestMapping(value = "/authorities")
@SecurityRequirement(name = "bearer-key")
public class AuthorityController {
	@Autowired AuthorityService authorityService;
	
	
	@GetMapping(value = "/currentUserAuthorities")
	public ResponseEntity<Set<String>> getReachableGrantedAuthorities(@CurrentSecurityContext SecurityContext securityContext) {
		return ResponseEntity.ok(authorityService.getReachableGrantedAuthorities(securityContext.getAuthentication().getAuthorities()));
	}
	
//	@GetMapping(value = "/{userId}")
	// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	// public ResponseEntity<Set<String>> getReachableGrantedAuthoritiesByUserId(@PathVariable String userId) {
		
	// 	return ResponseEntity.ok(authorityService.getReachableGrantedAuthorities(userId));
	// }

	
	 @PutMapping(value = "/assign/{userId}/{authority}")
	 @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	 public ResponseEntity<Set<String>> assignAuthorityToUser(@PathVariable String userId, @PathVariable String authority) {
	 	return ResponseEntity.ok(authorityService.assignAuthorityToUser(userId,authority.toUpperCase()));
	 }

//	@PutMapping(value = "/remove/{userId}/{authority}")
	// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	// public ResponseEntity<Set<String>> removeAuthorityFromUser(@PathVariable String userId, @PathVariable String authority) {
	// 	return ResponseEntity.ok(authorityService.assignAuthorityToUser(userId,authority));
	// }

}
