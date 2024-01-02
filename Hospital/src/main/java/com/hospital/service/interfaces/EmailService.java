package com.hospital.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.dto.EmailDetails;
import com.hospital.model.Email;

@Transactional
public interface EmailService {
	
	void sendMail(EmailDetails details);
	
	Email createEmail(Email email);

	Email updateEmail(Email email);

	void deleteEmail(Email email);

	Email getByEmail(String email);

	Email getEmail(Long id);

	Email getPrimaryEmailsOfUserByUseId(String user_id);

	Page<Email> getEmailsOfUser(String user_id, Pageable pageable);

}
