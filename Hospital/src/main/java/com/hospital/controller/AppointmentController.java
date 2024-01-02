package com.hospital.controller;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.AppointmentRequestDto;
import com.hospital.dto.DirectBookAppointmentRequest;
import com.hospital.enums.AppointmentStatus;
import com.hospital.model.Appointment;
import com.hospital.service.interfaces.AppointmentService;
import com.hospital.util.PdfGenerator;
import com.lowagie.text.DocumentException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")

public class AppointmentController {

	@Autowired private AppointmentService appointmentService;
	@PostMapping("/book")
//	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<Appointment> bookAppointment(@Valid @RequestBody AppointmentRequestDto appointmentDto, @RequestParam String departmentName){
		Appointment bookedAppointment = appointmentService.bookAppointment(appointmentDto.getPatient(), appointmentDto.getSlot(), departmentName);
		return new ResponseEntity<Appointment>(bookedAppointment , HttpStatus.CREATED);
	}
	@PostMapping("/direct-book")
	public ResponseEntity<Appointment> directBookAppointment(@Valid @RequestBody DirectBookAppointmentRequest directBookAppointment){
		Appointment bookedAppointment = appointmentService.directBookAppointment(directBookAppointment.getPatientInfo(), directBookAppointment.getDateTime());
		return new ResponseEntity<Appointment>(bookedAppointment , HttpStatus.CREATED);
	}
	@SecurityRequirement(name = "bearer-key")
	@PutMapping("/update")
	public ResponseEntity<Appointment> updateAppointment(@Valid @RequestBody AppointmentRequestDto appointmentDto){
		Appointment updateAppointment = appointmentService.updateAppointment(appointmentDto);
		return new ResponseEntity<Appointment>(updateAppointment , HttpStatus.CREATED);
	}
	@SecurityRequirement(name = "bearer-key")
	@PutMapping("/updatetatus")
	public ResponseEntity<Appointment> updateAppointmentStatus(Authentication authentication,@RequestParam Long appointmentId, @RequestParam AppointmentStatus appointmentStatus){
		Appointment updateAppointment = appointmentService.updateAppointmentStatus(authentication, appointmentId,appointmentStatus);
		return new ResponseEntity<Appointment>(updateAppointment , HttpStatus.CREATED);
	}
	
	@SecurityRequirement(name = "bearer-key")
	@GetMapping()
	public ResponseEntity<List<Appointment>> getAllBookedAppointment(){
		return new ResponseEntity<List<Appointment>>(appointmentService.getbookedAppointments(), HttpStatus.OK);
	}
	@GetMapping("/mobile")
	public ResponseEntity<List<Appointment>> getBookedAppointmentByMobile(@RequestParam String mobile){
		return new ResponseEntity<List<Appointment>>(appointmentService.getAppointmentByPatientMobileNumber(mobile), HttpStatus.OK);
	}
	@GetMapping("/email")
	public ResponseEntity<List<Appointment>> getBookedAppointmentByEmail(@RequestParam String email){
		return new ResponseEntity<List<Appointment>>(appointmentService.getAppointmentByPatientEmail(email), HttpStatus.OK);
	}
	@GetMapping("/email-mobile")
	public ResponseEntity<List<Appointment>> getBookedAppointmentByPatientEmailOrMobile(@RequestParam String emailOrMobile){
		return new ResponseEntity<List<Appointment>>(appointmentService.getAppointmentByPatientEmailOrMobile(emailOrMobile), HttpStatus.OK);
	}
	
	@GetMapping("/mobile-aadhar")
	public ResponseEntity<List<Appointment>> getAppointmentDetailsByPatientMobileAndAadhar(@RequestParam String mobile, @RequestParam String aadhar){
		return new ResponseEntity<List<Appointment>>(appointmentService.getAppointmentDetailsByPatientMobileAndAadhar(mobile, aadhar), HttpStatus.OK);
	}
	
    @GetMapping(value = "/download-invoice")
	public ResponseEntity<InputStreamResource> generatePdfFile(@RequestParam Long appointmentId) throws DocumentException, IOException {

    	Appointment appointment = appointmentService.getAppointment(appointmentId);
        ByteArrayInputStream pdf = PdfGenerator.generate(appointment);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=hospital.pdf");
        headers.add("Content-Type", "application/pdf");
        
        return new ResponseEntity<InputStreamResource>(new InputStreamResource(pdf),headers, HttpStatus.OK);
		
	 }
}

