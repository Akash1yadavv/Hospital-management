/**
 * 
 */
package com.hospital.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hospital.model.Email;


/**
 * 
 */
public interface EmailRepo extends JpaRepository<Email, Long> {
	
	 Optional<Email> findByEmail(String email);
	 @Query("select e from Email e where e.user.id = :user_id")
	 Page<Email> getEmailsOfUserByUseId(String user_id, Pageable pageable);
	 
	 @Query("select e from Email e where e.user.id = :user_id and e.prim=true")
	 Optional<Email> getPrimaryEmailsOfUserByUseId(String user_id);

}
