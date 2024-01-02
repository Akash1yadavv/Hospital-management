package com.hospital.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.OperationStatusDto;
import com.hospital.dto.user.ChangeEmailReqDto;
import com.hospital.dto.user.CurrentUserResDto;
import com.hospital.dto.user.UserProfileUpdateReqDto;
import com.hospital.model.EmailRequest;
import com.hospital.model.User;
import com.hospital.service.AuthEmailSender;
import com.hospital.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearer-key")
@Log4j2
public class UserController {

	@Autowired	private UserService userservice;
	@Autowired private AuthEmailSender authEmailSender;

	@GetMapping("/currentUser")
	public ResponseEntity<CurrentUserResDto> getCurrentUser(Authentication authentication){
		System.out.println(authentication);
		
		CurrentUserResDto user = userservice.getCurrentUserResDto(authentication.getName());
		return ResponseEntity.ok(user);
	}
	
	@PutMapping("/change-email")
	public ResponseEntity<OperationStatusDto> changeEmail(@RequestBody @Valid ChangeEmailReqDto changeEmailReqDto, Principal principal){
		User currentUser =  userservice.getUserByUsername(principal.getName());
		if(principal.getName().equals(currentUser.getId())) {
	        currentUser.setEmail(changeEmailReqDto.getEmail());
	        currentUser.setEmailVerified(false);
	        User updatedUser = userservice.updateprofile(currentUser);
	        authEmailSender.sendEmailVerificationMail(updatedUser);
	        return new ResponseEntity<OperationStatusDto>( new OperationStatusDto("Change email","Operation successfully. An email verification link has been sent to "+changeEmailReqDto.getEmail()+"."),HttpStatus.OK);	
		} else {
			throw new AccessDeniedException("forbidden");
		}
		
	}
	
	
	@PutMapping(value = "/profile")
	private ResponseEntity<OperationStatusDto> updateProfile(@RequestBody UserProfileUpdateReqDto userProfileUpdateReqDto, Principal principal) throws java.io.IOException {
		User currentUser =  userservice.getUserByUsername(principal.getName());
		if(principal.getName().equals(currentUser.getId())) {
	        currentUser.setName(userProfileUpdateReqDto.getName());
	        currentUser.setEnabled(userProfileUpdateReqDto.isEnabled());
	        userservice.updateprofile(currentUser);
	        return new ResponseEntity<OperationStatusDto>( new OperationStatusDto("upload image","File uploaded successfully"),HttpStatus.OK);	
		} else {
			throw new AccessDeniedException("forbidden");
		}
	}
	
	
	@PostMapping("/createuser")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<User> createuser(@RequestBody User user){
		
		User createuser = userservice.createUser(user);
		
		return new ResponseEntity<User> (createuser , HttpStatus.CREATED);
	}
	
	@GetMapping("/listuser")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<User>> getlistofuser(){
		
		List<User> listuser = userservice.getAllUser();
		
		return new ResponseEntity<List<User>>  (listuser , HttpStatus.OK);
	}
	
        
//	*/block User /*
	
	@PutMapping("/block-user/{userId}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> blockUser(@PathVariable String userId) {
		User user = userservice.blockuser(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("User blocked successfully");
    }
	
// */unblock User /*
	
	
	 @PutMapping("/unblock-user/{userId}")
	 @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	    public ResponseEntity<String> unblockUser(@PathVariable String userId) {
		 User user = userservice.unblockuser(userId);
	        if (user == null) {
	            return ResponseEntity.notFound().build();
	        }
	        return ResponseEntity.ok("User unblocked successfully");
	    }
	 
	 
	 @PostMapping("/send-to-all-users")
	 @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	    public void sendEmailToAllUsers(@RequestBody EmailRequest emailRequest) {
	        String subject = emailRequest.getSubject();
	        String text = emailRequest.getText();
	        userservice.sendEmailToAllUsers(subject, text);
	    }
	
}