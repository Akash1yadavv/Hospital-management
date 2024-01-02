package com.hospital.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.hospital.exception.PatientException;
import com.hospital.exception.UserException;
import com.hospital.model.PatientInfo;
import com.hospital.repo.PatientRepository;

import com.hospital.service.interfaces.PatientService;

public class PatientServiceImpl implements PatientService{

	
	@Autowired private PatientRepository patientRepository;
	
	@Override
	public PatientInfo registerPatient(PatientInfo patient) throws PatientException {
		Optional<PatientInfo> existingPatient = patientRepository.findByEmail(patient.getEmail());
				
		if(!existingPatient.isPresent()) {

			return patientRepository.save(patient);
			
		}else throw new UserException("User allready exist...");
		
	}
	@Override
	public PatientInfo getPatientByEmail(String email) throws PatientException {
		Optional<PatientInfo> patient = patientRepository.findByEmail(email);
		if(!patient.isPresent()) {
			throw new PatientException("patient is not present..."	);
		}
		return patient.get();
	}
	@Override
	public PatientInfo getPatientByMobile(String mobile) throws PatientException {
		Optional<PatientInfo> patient = patientRepository.findByMobile(mobile);
		if(!patient.isPresent()) {
			throw new PatientException("patient is not present..."	);
		}
		return patient.get();
	}

}
