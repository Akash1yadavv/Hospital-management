package com.hospital.repo;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.hospital.dto.user.CurrentUserResDto;
import com.hospital.model.Authority;
import com.hospital.model.Department;
import com.hospital.model.User;

import jakarta.persistence.LockModeType;

public interface UserRepository extends JpaRepository<User, String>{
	
	List<User> findAll();
	
	@Query("select u from User u where u.email=:email")
	Optional<User> getUserByEmail(String email);
	
	@Query("select u.authorities from User u where u.id=:userId")
	List<Authority> getAuthoritiesOfUserById(String userId);
	@Query("select u.departments from User u where u.id=:userId")
	List<Department> getDepartmentsOfUserById(String userId);
	
	@Query("select new com.hospital.dto.user.CurrentUserResDto(u.id, u.name, u.email, u.accountNonExpired,\n"
			+ "			u.accountNonLocked, u.credentialsNonExpired, u.enabled, u.emailVerified,\n"
			+ "			u.authProvider, u.authProviderId, u.createdOn, u.lastUpdatedOn, u.image,\n"
			+ "			u.blocked) from User u where u.id=:userId")
	Optional<CurrentUserResDto> getCurrentUserResDto(String userId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<User> findForUpdateById(String userId);
	
	Long countByCreatedOnLessThanAndCreatedOnGreaterThan(Instant sinceDate, Instant tillDate);

	

//	@Query("select u from User u join u.phoneNumberList e where e.phoneNumber=:phoneNumber")
//	User getUserByPhoneNumber(String phoneNumber);
	

}
