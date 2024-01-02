package com.hospital.controller;

import java.time.Instant;
import java.time.LocalDate;
//import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.AppointmentRequestDto;
import com.hospital.dto.admin.panel.AdminPanelStatistics;
import com.hospital.enums.AppointmentStatus;
import com.hospital.model.Appointment;
import com.hospital.model.Department;
import com.hospital.model.Slot;
import com.hospital.model.User;
import com.hospital.service.interfaces.AdminPanel;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(value = "/admin-panel")
@SecurityRequirement(name = "bearer-key")
@Log4j2
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminPanelController {
	
	@Autowired AdminPanel adminPanel;
	
//	*******************************SUPER ADMIN CONTROLLER *******************************
	
	@GetMapping(value = "/statistics")
	public AdminPanelStatistics getAdminPanelStatistics(@RequestParam Instant sinceInstant, @RequestParam Instant tillInstant) {
		log.debug("since "+ sinceInstant+" till "+tillInstant);
		return adminPanel.getAdminPanelStatistics(tillInstant, sinceInstant);
	}
	@GetMapping("/all-doctors")
	public ResponseEntity<List<User>> getAllDoctors(){
		List<User> users = adminPanel.getAllUser();
		return new ResponseEntity<List<User>>(users , HttpStatus.OK);
	}
	@GetMapping("/doctor-by-doctor-id")
	public ResponseEntity<User> getDoctorByDoctorId(@RequestParam String doctorId){
		User user = adminPanel.getUserByUserId(doctorId);
		return new ResponseEntity<User>(user , HttpStatus.OK);
	}
	@GetMapping("/doctor-by-doctor-email")
	public ResponseEntity<User> getDoctorByDoctorEmail(@RequestParam String email){
		User user = adminPanel.getUserByUserEmail(email);
		return new ResponseEntity<User>(user , HttpStatus.OK);
	}
	
	@GetMapping("/appointments")
	public ResponseEntity<List<Appointment>> getAllAppointments(){
		List<Appointment> list = adminPanel.getAllAppointment();
		return new ResponseEntity<List<Appointment>>(list , HttpStatus.OK);
	}
	@GetMapping("/finished-appointments")
	public ResponseEntity<List<Appointment>> getAllFinishedAppointments(){
		List<Appointment> list = adminPanel.getResolvedAppointments();
		return new ResponseEntity<List<Appointment>>(list , HttpStatus.OK);
	}
	@GetMapping("/scheduled-appointments")
	public ResponseEntity<List<Appointment>> getAllScheduledAppointments(){
		List<Appointment> list = adminPanel.getScheduledAppointments();
		return new ResponseEntity<List<Appointment>>(list , HttpStatus.OK);
	}
	@GetMapping("/canceled-appointments")
	public ResponseEntity<List<Appointment>> getAllCanceledAppointments(){
		List<Appointment> list = adminPanel.getCanceledAppointments();
		return new ResponseEntity<List<Appointment>>(list , HttpStatus.OK);
	}
	
	@GetMapping("/appointments-finished-by-doctor")
	public ResponseEntity<List<Appointment>> getAllAppointmentsFinishedByDoctor(@RequestParam String doctorId){
		List<Appointment> list = adminPanel.getAppointmentsFinishedByDoctor(doctorId);
		return new ResponseEntity<List<Appointment>>(list , HttpStatus.OK);
	}
	@GetMapping("/appointments-finished-by-doctor-user-name")
	public ResponseEntity<List<Appointment>> getAllAppointmentsFinishedByDoctorUserName(@RequestParam String userName){
		List<Appointment> list = adminPanel.getAppointmentsFinishedByDoctorUserName(userName);
		return new ResponseEntity<List<Appointment>>(list , HttpStatus.OK);
	}

//	*************************************Admin Controller ********************************

	@PutMapping("/update-appointment-status")
	public ResponseEntity<Appointment> updateAppointmentStatus(Authentication authentication,@RequestParam Long appointmentId, @RequestParam AppointmentStatus appointmentStatus){
		Appointment appointment = adminPanel.updateAppointmentStatus(authentication, appointmentId, appointmentStatus);
		return new ResponseEntity<Appointment>(appointment , HttpStatus.OK);
	}
	
	@PutMapping("/update-appointment")
	public ResponseEntity<Appointment> updateAppointment( @Valid @RequestBody AppointmentRequestDto appointmentRequestDto){
		Appointment appointment = adminPanel.updateAppointment(appointmentRequestDto);
		return new ResponseEntity<Appointment>(appointment , HttpStatus.OK);
	}
	@GetMapping("/appointments-finished-by-current-user")
	public ResponseEntity<List<Appointment>> getAllAppointmentsFinishedByCurrentUser(Authentication authentication){
		List<Appointment> list = adminPanel.getAppointmentsFinishedByCurrentUser(authentication);
		return new ResponseEntity<List<Appointment>>(list , HttpStatus.OK);
	}
	@GetMapping("/appointments-scheduled-by-department")
	public ResponseEntity<List<Appointment>> getAllScheduledAppointmentsByDepartments(@RequestParam String department){
		List<Appointment> list = adminPanel.getScheduledAppointmentsByDepartment(department);
		return new ResponseEntity<List<Appointment>>(list , HttpStatus.OK);
	}
	@GetMapping("/appointments-by-department")
	public ResponseEntity<List<Appointment>> getAllAppointmentsByDepartments(@RequestParam String department){
		List<Appointment> list = adminPanel.getAppointmentsByDepartment(department);
		return new ResponseEntity<List<Appointment>>(list , HttpStatus.OK);
	}
	//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@ApiImplicitParams({
        @ApiImplicitParam(
            name = "date",
            value = "Date in the format YYYY-MM-DD",
            required = true,
            dataType = "java.lang.String",
            paramType = "query"
        )
    })
	@PostMapping("/doctor-leave")
	public ResponseEntity<List<Slot>> doctorLeave(Authentication authentication, @RequestParam @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate date){
		List<Slot> list = adminPanel.getLeave(authentication, date);
		return new ResponseEntity<List<Slot>>(list , HttpStatus.OK);
	}
 
}
