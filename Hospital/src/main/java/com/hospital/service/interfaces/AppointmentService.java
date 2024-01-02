package com.hospital.service.interfaces;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;

import com.hospital.dto.AppointmentRequestDto;
import com.hospital.enums.AppointmentStatus;
import com.hospital.exception.AppointmentException;
import com.hospital.exception.NotFoundException;
import com.hospital.exception.SlotException;
import com.hospital.model.Appointment;
import com.hospital.model.PatientInfo;
import com.hospital.model.Slot;
import com.hospital.model.User;
import com.lowagie.text.Document;

public interface AppointmentService {

	public Appointment bookAppointment(PatientInfo patient, Slot slot, String departmentName) throws AppointmentException, SlotException;
	public Appointment directBookAppointment(PatientInfo patient,LocalDateTime dateTime) throws AppointmentException;
	public Appointment cancelAppointment(Long appointmentId) throws AppointmentException;
	public Appointment updateAppointment(AppointmentRequestDto appointmentRequestDto) throws AppointmentException;
	public Appointment updateAppointmentStatus(Authentication authentication,Long appointmentId, AppointmentStatus appointmentStatus) throws AppointmentException;
	public Appointment getAppointment(Long appointmentId) throws NotFoundException;
	public List<Appointment> getbookedAppointments() throws NotFoundException;
	public Appointment rescheduleAppointment(Long appointmentId) throws NotFoundException;
	public List<Appointment> getAppointmentByPatientMobileNumber(String mobile) throws NotFoundException;
	public List<Appointment> getAppointmentByPatientEmail(String email) throws NotFoundException;
	public List<Appointment> getAppointmentByPatientEmailOrMobile(String emailOrMobile) throws NotFoundException;
	public List<Appointment> getAppointmentDetailsByPatientMobileAndAadhar(String mobile, String aadhar)throws NotFoundException;
	public List<Appointment> getAppointments() throws NotFoundException;
	public List<Appointment> getResolvedAppointments() throws NotFoundException;
	public List<Appointment> getScheduledAppointments() throws NotFoundException;
	public List<Appointment> getCanceledAppointments() throws NotFoundException;
	public List<Appointment> getAppointmentsFinishedByDoctor(String doctorId) throws NotFoundException;
	public List<Appointment> getAppointmentsFinishedByDoctorUserName(String userName) throws NotFoundException;
	public List<Appointment> getAppointmentsFinishedByCurrentUser(Authentication authentication) throws NotFoundException;
	public List<Appointment> getScheduledAppointmentsByDepartment(String department) throws NotFoundException;
	public List<Appointment> getAppointmentsByDepartment(String department) throws NotFoundException;
	
	
	
	// Invoice service methods
//	public ByteArrayInputStream genrateInvoicePDF(Long appointmentId) throws AppointmentException;
//	public Document downloadPDF(long appointmentId) throws AppointmentException, com.lowagie.text.DocumentException, java.io.IOException;
}
