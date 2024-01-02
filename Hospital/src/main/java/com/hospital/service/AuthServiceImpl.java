package com.hospital.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.dto.auth.LoginDto;
import com.hospital.enums.AuthProvider;
import com.hospital.exception.GeneralException;
import com.hospital.media.MediaResource;
import com.hospital.model.Authority;
import com.hospital.model.Email;
import com.hospital.model.User;
import com.hospital.service.interfaces.AuthService;
import com.hospital.service.interfaces.AuthorityService;
import com.hospital.service.interfaces.EmailService;
import com.hospital.service.interfaces.UserService;
import com.hospital.util.JwtUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService{
	

    @Autowired private JwtUtil jwtUtil;
    @Value("${frontendBaseUrl}")
    private String frontendBaseUrl;
    @Value("${jwtEmailVerificationTokenTime}")
    private Long jwtEmailVerificationTokenTime;
    @Value("${jwtResetPasswordTokenTime}")
    private Long jwtResetPasswordTokenTime;
    @Autowired private UserService userService;
    @Autowired private AuthorityService authorityService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationProvider authenticationProvider;
    @Autowired private AuthEmailSender authEmailSender;
    @Autowired private EmailService emailService;
    
    
    
    @Override
    public User signup(User signupUser){
    	Optional<User> userOptional =  userService.getUserByEmail(signupUser.getEmail().toLowerCase());
			
		
    	if(userOptional.isPresent()) {
    		throw new GeneralException("User already exists.");
    	}
    	User user = signupUser;
    	user.setAccountNonExpired(true);
    	user.setAccountNonLocked(true);
    	user.setCredentialsNonExpired(true);
    	user.setEnabled(true);
    	
    	MediaResource image = new MediaResource();
        image.setOwner(user);
        user.setImage(image);
        if(user.getAuthProvider() == AuthProvider.local) {
        	user.setPassword(passwordEncoder.encode(user.getPassword()));
        	user.setEmailVerified(false);
        }
        
        
        Optional<Authority> authorityOptional = authorityService.getAuthorityByAuthority("ROLE_USER");
        
        if(authorityOptional.isEmpty()) {
        	throw new GeneralException("User role could not be assigned. Please contact support team.");
        }
        Authority authority = authorityOptional.get();
        user.getAuthorities().add(authority);
        User createdUser = userService.createUser(user);
        if(user.getAuthProvider() == AuthProvider.local) {
//        	authEmailSender.sendEmailVerificationMail(createdUser);
        }
        
        if(user.getAuthProvider() == AuthProvider.google) {
        	Email emailModel = new Email();
        	emailModel.setUser(user);
        	emailModel.setPrim(true);
        	emailModel.setVerified(true);
        	emailModel.setEmail(createdUser.getEmail());
        	emailService.createEmail(emailModel);
        }
        return createdUser;
    }
    
    @Override
	public String login(LoginDto loginDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getUsername().toLowerCase(),loginDto.getPassword());
        authentication = authenticationProvider.authenticate(authentication);
        User user = (User) authentication.getPrincipal();
        if(! user.isEnabled()) {
        	throw new GeneralException("Your account is disabled.");
        }
        
       return jwtUtil.createAccessToken(authentication);
	}

}
