package com.hospital.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

import com.hospital.dto.auth.LoginDto;
import com.hospital.model.User;

@Transactional
public interface AuthService {
	
	User signup(User signupUser);

	public String login(LoginDto loginDto);

}
