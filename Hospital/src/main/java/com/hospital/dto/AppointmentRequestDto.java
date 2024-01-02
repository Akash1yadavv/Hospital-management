package com.hospital.dto;

import com.hospital.model.PatientInfo;
import com.hospital.model.Slot;
import com.hospital.model.User;

import lombok.Data;

@Data
public class AppointmentRequestDto {

	private PatientInfo patient;
	private Slot slot;
}
