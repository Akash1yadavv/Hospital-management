/**
 * 
 */
package com.hospital.service;


import java.time.Instant;

/**
 * Atul Singh Bhadouria , Ankit
 */

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import org.springframework.stereotype.Service;

import com.hospital.dto.user.CurrentUserResDto;
import com.hospital.exception.GeneralException;
import com.hospital.exception.NotFoundException;
import com.hospital.exception.UserException;
import com.hospital.model.User;
import com.hospital.repo.UserRepository;
import com.hospital.service.interfaces.UserService;

/**
 * @author Akash Yadav
 *
 */

@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired private UserRepository userRepo;
	
	@Autowired private EmailSendingServiceImpl emailservice;
	

	@Override
	public User registerUser(User user) throws UserException{
		Optional<User> existingUser = userRepo.getUserByEmail(user.getEmail());
		
		if(!existingUser.isPresent()) {
			
//			if(user.getPassword()!=null) {
//				user.setPassword(passwordEncoder.encode(user.getPassword()));
//			}
			return userRepo.save(user);
			
		}else throw new UserException("User allready exist...");
	}

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.getUserByEmail(username).orElse(null);
		return user;
	}


	/**
	 * @param user
	 * @return
	 */
	@Override
	public User createUser(User user) {
		if(user != null)
		return userRepo.save(user);
		else 
			throw new GeneralException("User should not be null.");
	}


	@Override
	public Optional<User> getUserByEmail(String email) {
		return userRepo.getUserByEmail(email);
		
	}

	
	
	 

	@Override
	public User getUserByUsername(String id) {
		return userRepo.findById(id).orElseThrow(()-> new NotFoundException("User not found by id: "+id));
	}


	@Override
	public User updateprofile(User user) {
       
	   User existingUser = this.getUserByUsername(user.getId());
		
		if(existingUser != null) {
			
			return userRepo.save(user);
			
		}
		else 
			throw new IllegalArgumentException("User does not exist...");
		
	
		
		
	}
	
	@Override
	public User updateUser(User user) {
	   return userRepo.save(user);
	}


	@Override
	public List<User> getAllUser() {
		
		List<User> listuser = userRepo.findAll();
		
		if(listuser.isEmpty())
			
	    throw new UserException("No record found in the database");
		
		
		return listuser;
	}


	@Override
	public User blockuser(String userId) {
		
		User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
         user.setBlocked(true);
         userRepo.save(user);
        }else {
        	throw new GeneralException("This User is not found");
        }
        
        return user;
	  
	}


	@Override
	public User unblockuser(String userId) {
	
		User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
        	if (user.isBlocked()) {
                user.setBlocked(false);
                userRepo.save(user);
                
        	}else {
        		throw new GeneralException("This User is not found");
        	}
        }else {
        	
        	return null;
        }
        return user;
     }
	@Override
	 public void sendEmailToAllUsers(String subject, String text) {
	        List<User> users = userRepo.findAll();
	        for (User user : users) {
	            emailservice.sendSimpleMessage(user.getEmail(), subject, text);
	        }
	 }


	@Override
	public CurrentUserResDto getCurrentUserResDto(String userId) {
		
		CurrentUserResDto currentUserResDto = userRepo.getCurrentUserResDto(userId).orElseThrow(()-> new GeneralException("No record found."));
		currentUserResDto.setAuthorities(userRepo.getAuthoritiesOfUserById(userId));
		currentUserResDto.setDepartments(userRepo.getDepartmentsOfUserById(userId));
		
		return currentUserResDto;
	}


	@Override
	public User getUserById(String id) {
		// TODO Auto-generated method stub
		return this.getUserByUsername(id);
	}


	@Override
	public User getUserByIdForUpdate(String userId) {
		
		return userRepo.findForUpdateById(userId).orElseThrow(()-> new NotFoundException("User not found by id: "+userId));
	}


	@Override
	public long countByCreatedOnLessThanAndCreatedOnGreaterThan(Instant sinceDate, Instant tillDate) {
		
		return userRepo.countByCreatedOnLessThanAndCreatedOnGreaterThan(sinceDate, tillDate);
	}

}
	
	

