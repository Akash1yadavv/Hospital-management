package com.hospital.service.interfaces;

import com.hospital.exception.NotFoundException;
import com.hospital.exception.PatientException;
import com.hospital.exception.UserException;
import com.hospital.model.PatientInfo;
import com.hospital.model.User;

public interface PatientService {
	PatientInfo registerPatient(PatientInfo patient) throws PatientException;
	PatientInfo getPatientByEmail(String email) throws NotFoundException;
	PatientInfo getPatientByMobile(String mobile) throws NotFoundException;
}
