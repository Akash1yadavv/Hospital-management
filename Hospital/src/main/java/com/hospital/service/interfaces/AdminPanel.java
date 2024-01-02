package com.hospital.service.interfaces;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;

import com.hospital.dto.AppointmentRequestDto;
import com.hospital.dto.admin.panel.AdminPanelStatistics;
import com.hospital.enums.AppointmentStatus;
import com.hospital.exception.AppointmentException;
import com.hospital.exception.NotFoundException;
import com.hospital.exception.UserException;
import com.hospital.model.Appointment;
import com.hospital.model.Slot;
import com.hospital.model.User;

public interface AdminPanel {


	AdminPanelStatistics getAdminPanelStatistics(Instant sincInstant, Instant tillInstant);
	
//	***************SUPER ADMIN SERVICES**********************
	
	public User registerDocter(User user) throws UserException;
	public User deltetDocterByID(String id) throws UserException;
	public User updateUser(User user) throws UserException;
	public User getUserByUserName(User user) throws NotFoundException;
	public List<User> getAllUser() throws NotFoundException;
	public User getUserByUserId(String id) throws NotFoundException;
	public User getUserByUserEmail(String email) throws NotFoundException;
	public List<Appointment> getAllAppointment() throws NotFoundException;
	public List<Appointment> getResolvedAppointments() throws NotFoundException;
	public List<Appointment> getScheduledAppointments() throws NotFoundException;
	public List<Appointment> getCanceledAppointments() throws NotFoundException;
	public List<Appointment> getAppointmentsFinishedByDoctor(String Id) throws NotFoundException;
	public List<Appointment> getAppointmentsFinishedByDoctorUserName(String doctorUserName) throws NotFoundException;
	
//	***************************************ADMIN SERVICES**********************
	
	public Appointment updateAppointmentStatus(Authentication authentication, Long appointmentId, AppointmentStatus appointmentStatus) throws AppointmentException;
	public Appointment updateAppointment(AppointmentRequestDto appointmentRequestDto) throws AppointmentException;
	public List<Appointment> getAppointmentsFinishedByCurrentUser(Authentication authentication) throws NotFoundException;
	public List<Appointment> getScheduledAppointmentsByDepartment(String department) throws NotFoundException;
	public List<Appointment> getAppointmentsByDepartment(String department) throws NotFoundException;
	
	public List<Slot> getLeave(Authentication authentication, LocalDate date) throws NotFoundException;
	
	
	
}
