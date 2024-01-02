/**
 * 
 */
package com.hospital.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

import com.hospital.model.Otp;

/**
 * @author ankit
 */
@Transactional
public interface OtpService {
	Otp createOtp(Otp otp);
	Otp getOtp(String id);
	void deteteOtp(String id);
	void deleteExpiredOtps();
}
