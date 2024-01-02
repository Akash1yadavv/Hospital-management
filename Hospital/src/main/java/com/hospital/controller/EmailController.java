/**
 * 
 */
package com.hospital.controller;



import java.security.Principal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.OperationStatusDto;
import com.hospital.dto.auth.VeryfyEmailDto;
import com.hospital.dto.email.EmailReqDto;
import com.hospital.exception.GeneralException;
import com.hospital.model.Email;
import com.hospital.model.Otp;
import com.hospital.model.User;
import com.hospital.service.EmailSendingServiceImpl;
import com.hospital.service.interfaces.EmailService;
import com.hospital.service.interfaces.OtpService;
import com.hospital.service.interfaces.UserService;
import com.hospital.util.GeneralUtil;
import com.hospital.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;

/**
 * 
 */
@RestController
@RequestMapping(value = "/email")
@SecurityRequirement(name = "bearer-key")
@Log4j2
@Transactional
public class EmailController {
	
	@Autowired EmailService emailService;
	@Autowired UserService userService;
	@Autowired GeneralUtil generalUtil; 
    @Autowired private JwtUtil jwtUtil;
    @Autowired private EmailSendingServiceImpl emailSendingService;
    @Value("${frontendBaseUrl}")
    private String frontendBaseUrl;
    @Value("${jwtEmailVerificationTokenTime}")
    private Long jwtEmailVerificationTokenTime;
    @Value("${jwtResetPasswordTokenTime}")
    private Long jwtResetPasswordTokenTime;
    @Autowired OtpService otpService;
	
	@PostMapping(value = "/")
	public ResponseEntity<Email> addEmail(@RequestBody @Valid @NotNull EmailReqDto emailReqDto, Principal principal) {
		Page<Email> emailPage = emailService.getEmailsOfUser(principal.getName(), null);
		if(emailPage.getTotalElements() >= 5) {
			throw new GeneralException("Sorry, you can not add more the 5 email address.");
		}
		Email email = new Email();
		email.setEmail(emailReqDto.getEmail().toLowerCase());
		User currentUser = userService.getUserById(principal.getName());
		email.setUser(currentUser);
		email.setPrim(false);
		email.setVerified(false);
		Email savedEmail = emailService.createEmail(email);
		return ResponseEntity.ok(savedEmail); 
	}
		
	@GetMapping(value = "/")
	public ResponseEntity<Page<Email>> getAllEmail(Principal principal, Pageable pageable) {
		
		return ResponseEntity.ok(emailService.getEmailsOfUser(principal.getName(), pageable)); 
	}
	
	@DeleteMapping(value = "/")
	public ResponseEntity<String> deleteEmail(@RequestParam Long emailId, Principal principal) {
		
		Email email = emailService.getEmail(emailId);
		if(email.isPrim()){
			throw new GeneralException("Sorry, you cannot delete the primary email.");
		}
		if(principal.getName().equals(email.getUser().getId())) {
			emailService.deleteEmail(email);
		}else {
			throw new AccessDeniedException("forbidden");
		}
		return ResponseEntity.noContent().build(); 
	}
	
	
	@PutMapping(value = "/makeAnEmailPrimary")
	public ResponseEntity<String> makeAnEmailPrimary(@RequestParam Long emailId, Principal principal) {
		
		Email email = emailService.getEmail(emailId);
		
		
		if(!email.isVerified()){
			throw new GeneralException("Please, verify this email before making it primary.");
		}
		
		
		Optional<User> optionalUser = userService.getUserByEmail(email.getEmail());
		if(optionalUser.isEmpty()) {
			
			if(principal.getName().equals(email.getUser().getId())) {
				Email primaryEmail = emailService.getPrimaryEmailsOfUserByUseId(principal.getName());
				primaryEmail.setPrim(false);
				email.setPrim(true);
				emailService.updateEmail(primaryEmail);
				User currentUser = primaryEmail.getUser();
				currentUser.setEmail(email.getEmail());
				userService.updateUser(currentUser);
				
			}else {
				throw new AccessDeniedException("forbidden");
			}
			return ResponseEntity.noContent().build(); 
		}
		throw new GeneralException("You can not make this email primary because this email is already a primary email for another account.");
	}
	
	
	@GetMapping(value = "/verifyEmailRequest")
	public ResponseEntity<OperationStatusDto> verifyEmailRequest(@RequestParam Long emailId,  Principal principal) {
		Email email = emailService.getEmail(emailId);
		if(email.isVerified()) {
			throw new GeneralException("Email is already verified.");
		}
		if(email.getUser().getId().equals(principal.getName())) {
			User currentUser = userService.getUserById(principal.getName());
			String otpString = generalUtil.getRandomNumber6DigitString();
			Otp otp = new Otp();
			otp.setOtp(otpString);
			otp.setExpiresAt(Instant.now().plusSeconds(300));
			Otp createdOtp =  otpService.createOtp(otp);
			
	        Map<String, String> jwtMap = new HashMap<>();
	        jwtMap.put("emailId", ""+emailId);
	        jwtMap.put("type","emailVerification");
	        jwtMap.put("otpId", createdOtp.getId());
	        String jwtToken = jwtUtil.generateToken(jwtMap, jwtEmailVerificationTokenTime, "Email Verification");
	        
	        String emailVerificationUrl = frontendBaseUrl+"/email/verify?token="+jwtToken;
	        
	        Map<String, Object> emailMap = new HashMap<>();
	        emailMap.put("recipientName", currentUser.getName());
	        emailMap.put("verificationLink", emailVerificationUrl);
	        emailMap.put("jwtEmailVerificationTokenTime", (jwtEmailVerificationTokenTime / (1000*60)));
	        
	        Map<String, Object> otpEmailMap = new HashMap<>();
	        otpEmailMap.put("recipientName", currentUser.getName());
	        otpEmailMap.put("otp", otp.getOtp());
	        otpEmailMap.put("validFor", (jwtEmailVerificationTokenTime / (1000*60)));
	        otpEmailMap.put("purpose", "verify email ("+email.getEmail()+").");
	        
	        try {
				emailSendingService.sendMessageUsingThymeleafTemplate(email.getEmail(), "Email Verification", "verify-email.html", emailMap);
				emailSendingService.sendMessageUsingThymeleafTemplate(currentUser.getEmail(), "Email Verification OTP", "otp.html", otpEmailMap);
			} catch (MessagingException e) {
				throw new GeneralException(e.getMessage());
			}
			
			OperationStatusDto operationStatusDto = new OperationStatusDto();
			operationStatusDto.setOperation("Verify Email");
			operationStatusDto.setStatus("An verification link hand been sent to "+email.getEmail()+".");
			return ResponseEntity.ok(operationStatusDto); 
			
		}else {
			throw new AccessDeniedException("forbidden");
		}

	}
	
	@PatchMapping(value = "/verifyEmail")
	public ResponseEntity<OperationStatusDto> verifyEmail(@RequestBody @Valid @NotNull VeryfyEmailDto verifyEmailDto, Principal principal) {
		Claims claims;
    	try {
    		claims = jwtUtil.decodeToken(verifyEmailDto.getToken());
		} catch (JwtException e) {
			throw new GeneralException("Invalid token.");
		}
		Email email = emailService.getEmail(Long.parseLong(((String) claims.get("emailId"))));
		if(email.getUser().getId().equals(principal.getName())) {
	    	String type = (String) claims.get("type");
	    	if(!type.equals("emailVerification")) {
	    		throw new GeneralException("Not a valid token for this operation.");
	    	}
	    	
	    	String otpId = (String)claims.get("otpId");
	    	Otp otp = otpService.getOtp(otpId);
	    	if(otp.getOtp().equals(verifyEmailDto.getOtp())) {
	    		email.setVerified(true);
	    		emailService.updateEmail(email);
	    		otpService.deteteOtp(otpId);
	    	}else {
	    		throw new GeneralException("Invalid otp.");
	    	}
	    	
			OperationStatusDto operationStatusDto = new OperationStatusDto();
			operationStatusDto.setOperation("Email verification");
			operationStatusDto.setStatus("Email verification successful.");
			return new ResponseEntity<OperationStatusDto>(operationStatusDto,HttpStatus.OK);
			
		}else {
			throw new AccessDeniedException("forbidden");
		}

	}
	
	
	@Scheduled(fixedRate = 60000) 
	public void deleteExpiredOtpsTask() {
		otpService.deleteExpiredOtps();
	}

}
