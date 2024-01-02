/**
 * 
 */
package com.hospital.service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.exception.GeneralException;
import com.hospital.model.Otp;
import com.hospital.repo.OtpRepo;
import com.hospital.service.interfaces.OtpService;

/**
 * 
 */
@Service
public class OtpServiceImpl implements OtpService{
	@Autowired OtpRepo otpRepo;

	@Override
	public Otp createOtp(Otp otp) {
		if(otp == null)
			throw new IllegalArgumentException("Otp should not be null.");
		return otpRepo.save(otp);
	}

	@Override
	public Otp getOtp(String id) {
		Optional<Otp> otpOptional = otpRepo.findById(id);
		if(otpOptional.isEmpty()) {
			throw new GeneralException("Otp is not valid.");
		} else {
			Otp otp = otpOptional.get();
			Duration duration = Duration.between(otp.getExpiresAt(), Instant.now());
			if(duration.abs().getSeconds() >= 0) {
				otp.setReadCount(otp.getReadCount()+1);
				if(otp.getReadCount() > 5) {
					this.deteteOtp(id);
					throw new GeneralException("Maximum OTP attempts exceeded. Please request a new OTP.");
				}
				return otp;
			} else {
				this.deteteOtp(id);
				throw new GeneralException("Otp expired.");
			}
		}
			
		
	}

	@Override
	public void deteteOtp(String id) {
		otpRepo.deleteById(id);
		
	}
	
	public void deleteExpiredOtps() {
        Instant currentTimestamp = Instant.now().minus(5, ChronoUnit.MINUTES); 
        otpRepo.deleteExpiredOtps(currentTimestamp);
    }

}
