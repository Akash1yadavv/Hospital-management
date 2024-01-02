package com.hospital.service.interfaces;

import java.time.Instant;

/**
 * Atul Singh Bhadouria , Ankit
 */


import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.dto.user.CurrentUserResDto;
import com.hospital.exception.UserException;
import com.hospital.model.User;



@Transactional
public interface UserService extends UserDetailsService{
	User registerUser(User user) throws UserException;
    User createUser(User user);
    Optional<User> getUserByEmail(String email);
	User getUserByUsername(String id);
	User getUserById(String id);
	User updateprofile(User user);
	public List<User> getAllUser();
	User updateUser(User user);
	public User blockuser (String userId);
	public User unblockuser (String userId);
	public void sendEmailToAllUsers(String subject, String text);
	CurrentUserResDto getCurrentUserResDto(String userId);
	User getUserByIdForUpdate(String userId);
	long countByCreatedOnLessThanAndCreatedOnGreaterThan(Instant sinceDate, Instant tillDate);
//	User save(User user);
}

