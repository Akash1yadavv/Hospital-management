package com.hospital.repo;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hospital.model.Otp;

import jakarta.transaction.Transactional;

public interface OtpRepo extends JpaRepository<Otp, String>{

	@Transactional
	@Modifying
    @Query("DELETE FROM Otp o WHERE o.expiresAt < :currentTimestamp")
    void deleteExpiredOtps(@Param("currentTimestamp") Instant currentTimestamp);
}
