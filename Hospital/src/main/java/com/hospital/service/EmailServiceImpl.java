package com.hospital.service;

import java.io.File;
import java.util.Optional;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.hospital.dto.EmailDetails;
import com.hospital.exception.GeneralException;
import com.hospital.exception.EmailException;
import com.hospital.model.Email;
import com.hospital.repo.EmailRepo;
import com.hospital.service.interfaces.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
	@Autowired EmailRepo emailRepo;

	

	@Autowired private JavaMailSender emailSender;
	@Autowired Environment environment;
	
	@Value("${spring.mail.username}") 
	private String sender;
	
	@Override
	public void sendMail(EmailDetails details) {
		
		MimeMessage message = emailSender.createMimeMessage();
	   
	    MimeMessageHelper mimeMessageHelper;
	   
	    try {
            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper = new MimeMessageHelper(message, true,"utf-8");
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody(),details.getIsHtml());
            
            mimeMessageHelper.setSubject(
                details.getSubject());
 
            if(details.getAttachment() != null) {
            	 FileSystemResource file
                 = new FileSystemResource(
                     new File(details.getAttachment()));
  
	             mimeMessageHelper.addAttachment(
	                 file.getFilename(), file);
            }
            // Sending the mail
            emailSender.send(message);
        }
        catch (MessagingException e) {
        	
            throw new EmailException(e.getMessage());
        }
    
	}
	
	@Override
	public Email createEmail(Email email) {
		if(email == null)
			throw new IllegalIdentifierException("Email should not be null.");
		return emailRepo.save(email);
	}

	@Override
	public Email updateEmail(Email email) {
		this.getEmail(email.getId());
		return this.createEmail(email);
	}

	@Override
	public void deleteEmail(Email email) {
		this.getEmail(email.getId());
		emailRepo.delete(email);
	}

	@Override
	public Email getByEmail(String email) {
		Optional<Email> foundEmail = emailRepo.findByEmail(email);
		if(foundEmail.isEmpty()) {
			throw new GeneralException("No email found by id "+email);
		}
		return foundEmail.get();
	}

	@Override
	public Page<Email> getEmailsOfUser(String user_id, Pageable pageable) {
		
		return emailRepo.getEmailsOfUserByUseId(user_id, pageable);
	}

	@Override
	public Email getPrimaryEmailsOfUserByUseId(String user_id) {
		Optional<Email> foundEmail = emailRepo.getPrimaryEmailsOfUserByUseId(user_id);
		if(foundEmail.isEmpty()) {
			throw new GeneralException("No primary email found for userId "+user_id);
		}
		return foundEmail.get();
	}

	@Override
	public Email getEmail(Long id) {
		Optional<Email> foundEmail = emailRepo.findById(id);
		if(foundEmail.isEmpty()) {
			throw new GeneralException("No email found by id "+id);
		}
		return foundEmail.get();
	}

}
