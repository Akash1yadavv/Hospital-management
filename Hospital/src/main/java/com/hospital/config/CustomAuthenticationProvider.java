package com.hospital.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hospital.model.User;
import com.hospital.service.AuthEmailSender;
import com.hospital.service.AuthServiceImpl;
import com.hospital.service.interfaces.UserService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Configuration
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthEmailSender authEmailSender;
    

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = (User) userService.loadUserByUsername(authentication.getName());
        if(user == null ) {
        	throw new BadCredentialsException("invalid credentials"); 
        }else if(user.getPassword() == null) {
        	authEmailSender.sendPasswordResetMail(user);
        	throw new BadCredentialsException("You have not set your password yet. If you wish to set your password, please follow the link sent to your registered email to set your password."); 
        }else if( ! passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())){
        	throw new BadCredentialsException("Invalid username or password."); 
        }else if(!user.isEmailVerified()){
        	authEmailSender.sendEmailVerificationMail(user);
        	throw new BadCredentialsException("Please verify you email.");
        }else if(user.isBlocked()) {
        	 throw new BadCredentialsException("Your account is blocked. Please, contact our support team."); 
        }else if(!user.isAccountNonExpired() || !user.isAccountNonLocked() || !user.isCredentialsNonExpired()) {
        	throw new BadCredentialsException("Your account is frozen. Please contact our support team.");
        }else if(! user.isEnabled()) {
        	new BadCredentialsException("Your account is disabled. Please contact our support team.");
        }
        
        return new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
