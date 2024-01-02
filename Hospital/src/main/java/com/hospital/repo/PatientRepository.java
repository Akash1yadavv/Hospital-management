package com.hospital.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.model.PatientInfo;
import com.hospital.model.User;

public interface PatientRepository extends JpaRepository<PatientInfo, Long>{
	public Optional<PatientInfo> findByEmail(String email);
	public Optional<PatientInfo> findByMobile(String mobile);
}
