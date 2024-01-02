package com.hospital.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hospital.exception.GeneralException;
import com.hospital.model.User;
import com.hospital.util.JwtUtil;

import jakarta.mail.MessagingException;

@Component
public class AuthEmailSender {
    @Autowired private JwtUtil jwtUtil;
    @Autowired private EmailSendingServiceImpl emailService;
    @Value("${frontendBaseUrl}")
    private String frontendBaseUrl;
    @Value("${jwtEmailVerificationTokenTime}")
    private Long jwtEmailVerificationTokenTime;
    @Value("${jwtResetPasswordTokenTime}")
    private Long jwtResetPasswordTokenTime;
	

    public void sendEmailVerificationMail(User user) {
        Map<String, String> jwtMap = new HashMap<>();
        jwtMap.put("email", user.getEmail());
        jwtMap.put("type","emailVerification");
        String jwtToken = jwtUtil.generateToken(jwtMap, jwtEmailVerificationTokenTime, "Email Verification");
        String emailVerificationUrl = frontendBaseUrl+"/auth/verify-email?token="+jwtToken;
        
        Map<String, Object> emailMap = new HashMap<>();
        emailMap.put("recipientName", user.getName());
        emailMap.put("verificationLink", emailVerificationUrl);
        emailMap.put("jwtEmailVerificationTokenTime", (jwtEmailVerificationTokenTime / (1000*60)));
        try {
			emailService.sendMessageUsingThymeleafTemplate(user.getEmail(), "Email Verification", "verify-email.html", emailMap);
		} catch (MessagingException e) {
			throw new GeneralException(e.getMessage());
		}
    }
    

    public void sendPasswordResetMail(User user) {
    	Map<String, String> jwtMap = new HashMap<>();
		jwtMap.put("email", user.getEmail());
		jwtMap.put("type","restPassword");
		String jwtToken = jwtUtil.generateToken(jwtMap, jwtResetPasswordTokenTime, "Reset Password");
		
		String resetPasswordLink = frontendBaseUrl+"/auth/reset-password?token="+jwtToken;
		
		Long jwtResetPasswordTokenTimeInMinute = jwtResetPasswordTokenTime/(1000*60);
		
		Map<String, Object> emailMap = new HashMap<>();
        emailMap.put("resetPasswordLink", resetPasswordLink);
        emailMap.put("jwtResetPasswordTokenTime", jwtResetPasswordTokenTimeInMinute);
        emailMap.put("recipientName", user.getName());
        try {
			emailService.sendMessageUsingThymeleafTemplate(user.getEmail(), "Reset Password", "reset-password.html", emailMap);
		} catch (MessagingException e) {
			throw new GeneralException(e.getMessage());
		}
	}
}
