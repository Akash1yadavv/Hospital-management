package com.hospital.dto;

import java.time.LocalDateTime;

import com.hospital.model.PatientInfo;

import lombok.Data;

@Data
public class DirectBookAppointmentRequest {

	private PatientInfo patientInfo;
	private LocalDateTime dateTime;
}
