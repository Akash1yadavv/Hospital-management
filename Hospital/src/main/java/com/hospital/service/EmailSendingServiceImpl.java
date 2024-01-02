package com.hospital.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.hospital.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EmailSendingServiceImpl {
	 @Autowired
	 private JavaMailSender emailSender;
	 @Autowired
	 private SpringTemplateEngine thymeleafTemplateEngine;
	 @Value("${spring.mail.username}")
	 private String sentFrom;

	 
	 public void sendSimpleMessage(
			 
		    String to, String subject, String text) {  
	        SimpleMailMessage message = new SimpleMailMessage(); 
	        message.setFrom(sentFrom);
	        message.setTo(to); 
	        message.setSubject(subject); 
	        message.setText(text);
	        
	        emailSender.send(message);
	}
	 
	 
////	 @Override
//	 public void sendMessageWithAttachment(
//	   String to, String subject, String text, String pathToAttachment) throws MessagingException {
//	    
//	     MimeMessage message = emailSender.createMimeMessage();
//	      
//	     MimeMessageHelper helper = new MimeMessageHelper(message, true);
//	     
//	     helper.setFrom("noreply@baeldung.com");
//	     helper.setTo(to);
//	     helper.setSubject(subject);
//	     helper.setText(text);
//	         
//	     FileSystemResource file 
//	       = new FileSystemResource(new File(pathToAttachment));
//	     helper.addAttachment("Invoice", file);
//
//	     emailSender.send(message);
//	     // ...
//	 }
	 
	 
	 private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
		    MimeMessage message = emailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		    helper.setFrom(sentFrom);
		    helper.setTo(to);
		    helper.setSubject(subject);
		    helper.setText(htmlBody, true);
		    emailSender.send(message);
		}
	 
	 
	

//	 @Override
	 public void sendMessageUsingThymeleafTemplate(
	     String to, String subject, String template, Map<String, Object> templateModel)
	         throws MessagingException {
	                 
	     Context thymeleafContext = new Context();
	     thymeleafContext.setVariables(templateModel);
	     String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);
	     
	     sendHtmlMessage(to, subject, htmlBody);
	 }
	 
	 
	 



}