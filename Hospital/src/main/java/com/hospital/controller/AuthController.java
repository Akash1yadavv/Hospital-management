package com.hospital.controller;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.OperationStatusDto;
import com.hospital.dto.auth.LoginDto;
import com.hospital.dto.auth.LoginResDto;
import com.hospital.dto.auth.PasswordResetReq;
import com.hospital.dto.auth.PasswordResetRequestDto;
import com.hospital.dto.auth.SignupDto;
import com.hospital.dto.auth.VeryfyEmailDto;
import com.hospital.enums.AuthProvider;
import com.hospital.exception.GeneralException;
import com.hospital.model.Email;
import com.hospital.model.User;
import com.hospital.service.EmailSendingServiceImpl;
import com.hospital.service.interfaces.AuthService;
import com.hospital.service.interfaces.EmailService;
import com.hospital.service.interfaces.UserService;
import com.hospital.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
//@Transactional
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailSendingServiceImpl emailSendingService;
    @Autowired private AuthService authService;
    @Value("${frontendBaseUrl}")
    private String frontendBaseUrl;
    @Value("${jwtEmailVerificationTokenTime}")
    private Long jwtEmailVerificationTokenTime;
    @Value("${jwtResetPasswordTokenTime}")
    private Long jwtResetPasswordTokenTime;
    @Autowired private EmailService emailService;
    
    
//    @GetMapping(value = "/test")
//    public ResponseEntity<String> test(HttpServletRequest request){
//    	Enumeration<String> enumeration = request.getHeaderNames();
//    	
//    	while(enumeration.hasMoreElements()) {
//    		String name = enumeration.nextElement();
//    		System.out.println(name+" : "+ request.getHeader(name));
//    		
//    	}
//    	
//    	System.out.println();
//    	return ResponseEntity.ok(request.toString());
//    }

    @PostMapping(value = "/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid  SignupDto signupDto){
    	User user = new User();
    	user.setName(signupDto.getName());
    	user.setEmail(signupDto.getEmail());
    	user.setPassword(signupDto.getPassword());
    	user.setAuthProvider(AuthProvider.local);
    	User createdUser = authService.signup(user);
        
        return new ResponseEntity<User>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResDto> login(@RequestBody LoginDto loginDto){

        LoginResDto loginResDto = new LoginResDto();
        loginResDto.setType("Bearer");
        loginResDto.setAccessToken(authService.login(loginDto));
        return ResponseEntity.ok(loginResDto);

    }
    
    
    @PostMapping(value = "/reset-password-request")
    public ResponseEntity<OperationStatusDto> passwordResetRequest(@Valid @RequestBody PasswordResetRequestDto passwordResetRequestDto) {
    	User user = userService.getUserByEmail(passwordResetRequestDto.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User not found."));
    	
		Map<String, String> jwtMap = new HashMap<>();
		jwtMap.put("email", passwordResetRequestDto.getEmail());
		jwtMap.put("type","restPassword");
		String jwtToken = jwtUtil.generateToken(jwtMap, jwtResetPasswordTokenTime, "Reset Password");
		
		String resetPasswordLink = frontendBaseUrl+"/auth/reset-password?token="+jwtToken;
		
		Long jwtResetPasswordTokenTimeInMinute = jwtResetPasswordTokenTime/(1000*60);
		
		Map<String, Object> emailMap = new HashMap<>();
        emailMap.put("resetPasswordLink", resetPasswordLink);
        emailMap.put("jwtResetPasswordTokenTime", jwtResetPasswordTokenTimeInMinute);
        emailMap.put("recipientName", user.getName());
        try {
        	emailSendingService.sendMessageUsingThymeleafTemplate(passwordResetRequestDto.getEmail(), "Reset Password", "reset-password.html", emailMap);
		} catch (MessagingException e) {
			throw new GeneralException(e.getMessage());
		}
		OperationStatusDto operationStatusDto = new OperationStatusDto();
		operationStatusDto.setOperation("Reset Password");
		operationStatusDto.setStatus("To reset the password, please follow the instructions sent on your registered email.");
		return new ResponseEntity<OperationStatusDto>(operationStatusDto,HttpStatus.OK);
	}
  
    @PostMapping(value = "/reset-password")
    public ResponseEntity<OperationStatusDto> passwordReset(@Valid @RequestBody PasswordResetReq passwordResetReq) {
    	Claims claims;
    	try {
    		claims = jwtUtil.decodeToken(passwordResetReq.getToken());
		} catch (JwtException e) {
			throw new GeneralException("Invalid token.");
		}
    	
    	String email = (String) claims.get("email");
    	String type = (String) claims.get("type");
    	if(!type.equals("restPassword")) {
    		throw new GeneralException("Not a valid token for this operation.");
    	}
    	User user = userService.getUserByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found."));
    	
    	String hashedPass = passwordEncoder.encode(passwordResetReq.getNewPassword());
    	user.setPassword(hashedPass);
    	userService.updateUser(user);
		OperationStatusDto operationStatusDto = new OperationStatusDto();
		operationStatusDto.setOperation("Reset Password");
		operationStatusDto.setStatus("Password reset sucessfully.");
		return new ResponseEntity<OperationStatusDto>(operationStatusDto,HttpStatus.OK);
		
	}
    
  
    @PostMapping(value = "/verify-email")
    public ResponseEntity<OperationStatusDto>  varifyEmail(@RequestBody VeryfyEmailDto verifyEmailDto) {
    	
    	Claims claims;
    	try {
    		claims = jwtUtil.decodeToken(verifyEmailDto.getToken());
		} catch (JwtException e) {
			throw new GeneralException("Invalid token.");
		}
    	String email = (String) claims.get("email");
    	String type = (String) claims.get("type");
    	if(!type.equals("emailVerification")) {
    		throw new GeneralException("Not a valid token for this operation.");
    	}

    	User user = userService.getUserByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found."));
    	user.setEmailVerified(true);
    	userService.updateUser(user);
    	Email emailModel = new Email();
    	emailModel.setUser(user);
    	emailModel.setPrim(true);
    	emailModel.setVerified(true);
    	emailModel.setEmail(user.getEmail());
    	emailService.createEmail(emailModel);
		OperationStatusDto operationStatusDto = new OperationStatusDto();
		operationStatusDto.setOperation("Reset Password");
		operationStatusDto.setStatus("Email verification successful.");
		return new ResponseEntity<OperationStatusDto>(operationStatusDto,HttpStatus.OK);
	}
    
    

    

    
    
}
