package com.hospital.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.hospital.dto.AppointmentRequestDto;
import com.hospital.dto.EmailDetails;
import com.hospital.dto.user.CurrentUserResDto;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.InvoiceStatus;
import com.hospital.enums.PaymentStatus;
import com.hospital.enums.SlotAvailabilityStatus;
import com.hospital.exception.AppointmentException;
import com.hospital.exception.NotFoundException;
import com.hospital.exception.SlotException;
import com.hospital.exception.UserException;
import com.hospital.model.Appointment;
import com.hospital.model.Department;
import com.hospital.model.PatientInfo;
import com.hospital.model.Slot;
import com.hospital.model.User;
import com.hospital.repo.AppointmentRepository;
import com.hospital.repo.CategoryRepository;
import com.hospital.repo.DepartmentRepository;
import com.hospital.repo.PatientRepository;
import com.hospital.repo.SlotRepository;
import com.hospital.repo.UserRepository;
import com.hospital.service.interfaces.AppointmentService;
import com.hospital.service.interfaces.EmailService;
import com.hospital.service.interfaces.PatientService;
import com.hospital.service.interfaces.UserService;
import com.hospital.util.PdfGenerator;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;




@Service
public class AppointmentServiceImpl implements AppointmentService{


	@Autowired private AppointmentRepository appointmentRepository;
	@Autowired private UserService userService;
	@Autowired private PatientRepository patientRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private DepartmentRepository departmentRepository;
	@Autowired private EmailService emailService;
	@Autowired private SlotRepository slotRepository;
	
	private Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);
	
	@Override
	public Appointment bookAppointment(PatientInfo patient, Slot slot, String departmentName) throws AppointmentException,SlotException {
        Appointment appointment = new Appointment();
        
		Optional<Department> department = departmentRepository.findByName(departmentName);
        Slot slot2= new Slot();
        
        appointment.setStartTime(slot.getStartTime());
        appointment.setEndTime(slot.getStartTime().plusMinutes(60));
        
        if(patient==null || slot==null || department.isEmpty()) {
        	throw new AppointmentException("user or slot or department is null or empty...");
        }
        Optional<Slot> findedSlot = slotRepository.getBookedSlotsByDepartmentAndDateTime(departmentName, slot.getStartTime());
        if(findedSlot.isPresent()) {
        	throw new SlotException("Please select another slot, this slot has been booked..");
        }
        
        slot2.setStartTime(slot.getStartTime());
        slot2.setEndTime(slot.getStartTime().plusMinutes(60));
        slot2.setAvailabilityStatus(SlotAvailabilityStatus.BOOKED);
        slot2.setDepartment(department.get());
        slot2.setAvailabilityStatus(SlotAvailabilityStatus.BOOKED);
        
//        if(!existingUser.isPresent()) {
        	appointment.setPatientInfo(patient);
//        }

        appointment.setSlot(slot2);
        
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setPaymentStatus(checkPaymentStatus());
        
        appointmentRepository.save(appointment);
//        Send details by email address
		EmailDetails emailDetails = new EmailDetails();
		
		emailDetails.setSubject("Appointment Confirmation - Haarmk Hospital ");
		emailDetails.setMsgBody(buildEmailBody(patient.getPatientName(), slot.getStartTime(), departmentName, "Dr. Harveer"));
		emailDetails.setRecipient(patient.getEmail());
		emailDetails.setAttachment(null);
		emailDetails.setIsHtml(false);
		
		emailService.sendMail(emailDetails);
		return appointment;
	
	}
	
	@Override
	public Appointment directBookAppointment(PatientInfo patient, LocalDateTime dateTime) throws AppointmentException {
        Appointment appointment = new Appointment();
//      LocalDateTime start=LocalDateTime.now();
//      Optional<PatientInfo> existingPatient = patientRepository.findByEmail(patient.getEmail());
      
      Slot slot2= new Slot();
      
      appointment.setStartTime(dateTime);
      appointment.setEndTime(dateTime.plusMinutes(60));
      
      if(patient==null || dateTime==null ) {
      	throw new AppointmentException("user or slot or department is null or empty...");
      }
      slot2.setStartTime(dateTime);
      slot2.setEndTime(dateTime.plusMinutes(60));
      slot2.setAvailabilityStatus(SlotAvailabilityStatus.BOOKED);
      
//      if(!existingUser.isPresent()) {
      	appointment.setPatientInfo(patient);
//      }
      appointment.setSlot(slot2);
      
      appointment.setStatus(AppointmentStatus.SCHEDULED);
      appointment.setPaymentStatus(checkPaymentStatus());
      
      appointmentRepository.save(appointment);
//      Send details by email address
		EmailDetails emailDetails = new EmailDetails();
		
		emailDetails.setSubject("Appointment Confirmation - Haarmk Hospital ");
		emailDetails.setMsgBody(buildEmailBody(patient.getPatientName(), dateTime, "Direct Booked", "Dr. Harveer"));
		emailDetails.setRecipient(patient.getEmail());
		emailDetails.setAttachment(null);
		emailDetails.setIsHtml(false);
		
		emailService.sendMail(emailDetails);
		return appointment;
	}
	
	@Override
	public Appointment updateAppointmentStatus(Authentication authentication, Long appointmentId, AppointmentStatus appointmentStatus) throws AppointmentException {
		Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
		Optional<User> user = userRepository.findById(authentication.getName());
		
		if(!user.isPresent()) {
			throw new NotFoundException("please first you have to login..");
		}
		if(!appointment.isPresent()) {
			throw new AppointmentException("Not fount any appointment for appointmentId " + appointmentId);
		}
		Appointment appointment2 = appointment.get();
		appointment2.setStatus(appointmentStatus);
		appointment2.setUser(user.get());
		
		return appointmentRepository.save(appointment2);
	}

	@Override
	public Appointment updateAppointment(AppointmentRequestDto appointmentRequestDto) throws AppointmentException {
		Optional<Appointment> appointment = appointmentRepository.findAppointmentByPatientId(appointmentRequestDto.getPatient().getId());
		if(!appointment.isPresent()) {
			throw new AppointmentException("Not fount any appointment for user id " + appointmentRequestDto.getPatient().getId());
		}
		
		return appointmentRepository.save(appointment.get());
	}
	

	@Override
	public Appointment cancelAppointment(Long appointmentId) throws AppointmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appointment getAppointment(Long appointmentId) throws NotFoundException {
	
		Optional<Appointment> appointment = appointmentRepository.findByAppointmentId(appointmentId);
	
		if(appointment.isEmpty()){
			throw new NotFoundException("Ther is not available any appointment by this appointmentId;- "+ appointmentId);
		}
		return appointment.get();
	}
	@Override
	public List<Appointment> getbookedAppointments() throws NotFoundException {
		List<Appointment> bookedAppointments = appointmentRepository.findAll();
		if (bookedAppointments.size()==0) {
			throw new NotFoundException("There are no booking...");
		}
		return bookedAppointments;
	}

	@Override
	public Appointment rescheduleAppointment(Long appointmentId) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Appointment> getAppointmentByPatientMobileNumber(String mobile) throws NotFoundException {
		List<Appointment> appointmentList = appointmentRepository.findByPatientMobileNumber(mobile);
		if (appointmentList == null) {
			throw new NotFoundException("There is no appointment....");
		}
		return appointmentList;
	}


	@Override
	public List<Appointment> getAppointmentByPatientEmail(String email) throws NotFoundException {
		List<Appointment> appointmentList = appointmentRepository.findByPatientEmail(email);
		if (appointmentList == null) {
			throw new NotFoundException("There is no appointment....");
		}
		return appointmentList;
	}
	
	public  PaymentStatus checkPaymentStatus(){
		// here we have to write logic 
		return PaymentStatus.SUCCESSFULL;
	}
    private String buildEmailBody(String patientName, LocalDateTime appointmentDate, String departmentName, String doctorName) {
        // You can use StringBuilder or String.format to construct the email body
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Dear ").append(patientName).append(",\n\n");
        emailBody.append("We are pleased to inform you that your appointment at Haarmk Hospital has been successfully booked. Here are the details:\n\n");
        emailBody.append("- Time: ").append(appointmentDate.toString().substring(11)).append("\n");
        emailBody.append("- Date: ").append(appointmentDate.toString().substring(0, 10)).append("\n");
        emailBody.append("- Day: ").append(appointmentDate.getDayOfWeek()).append("\n");
        emailBody.append("- Department: ").append(departmentName).append("\n");
        emailBody.append("- Doctor: ").append(doctorName).append("\n\n");
        emailBody.append("Please make a note of the appointment details and arrive a bit earlier to complete any necessary paperwork.\n\n");
        emailBody.append("If you have any questions or need to reschedule, please contact us at 9598995079.\n\n");
        emailBody.append("Thank you for choosing Haarmk Hospital. We look forward to serving you.\n\n");
        emailBody.append("Best regards,\nHaarmk Infotech\n");

        return emailBody.toString();
    }

	@Override
	public List<Appointment> getAppointmentByPatientEmailOrMobile(String emailOrMobile) throws NotFoundException {
		List<Appointment> appointmentList =new ArrayList<Appointment>();
		if(emailOrMobile.contains("@")) {
			appointmentList = appointmentRepository.findByPatientEmail(emailOrMobile);
		}else {
			appointmentList = appointmentRepository.findByPatientMobileNumber(emailOrMobile);
		}
		if (appointmentList.size()==0) {
			throw new NotFoundException("There is no appointment....");
		}
		return appointmentList;
	}
	@Override
	public List<Appointment> getAppointmentDetailsByPatientMobileAndAadhar(String mobile, String aadhar) throws NotFoundException {
		List<Appointment> appointmentList = appointmentRepository.findByPatientMobileAndAadhar(mobile, aadhar);
		if (appointmentList.size()==0) {
			throw new NotFoundException("There is no appointment....");
		}
		return appointmentList;
	}

	@Override
	public List<Appointment> getAppointments() throws NotFoundException {
		List<Appointment> appointmentList = appointmentRepository.findAll();
		if (appointmentList.size()==0) {
			throw new NotFoundException("There is no appointment....");
		}
		return appointmentList;
	}

	@Override
	public List<Appointment> getResolvedAppointments() throws NotFoundException {
		List<Appointment> appointmentList = appointmentRepository.findFinishedAppointments();
		if (appointmentList.size()==0) {
			throw new NotFoundException("There is not any finished appointment....");
		}
		return appointmentList;
	}

	@Override
	public List<Appointment> getScheduledAppointments() throws NotFoundException {
		List<Appointment> appointmentList = appointmentRepository.findScheduledAppointments();
		if (appointmentList.size()==0) {
			throw new NotFoundException("There is not any finished appointment....");
		}
		return appointmentList;
	}

	@Override
	public List<Appointment> getCanceledAppointments() throws NotFoundException {
		List<Appointment> appointmentList = appointmentRepository.findCanceledAppointments();
		if (appointmentList.size()==0) {
			throw new NotFoundException("There is not any finished appointment....");
		}
		return appointmentList;
	}

	@Override
	public List<Appointment> getAppointmentsFinishedByDoctor(String doctorId) throws NotFoundException {
		Optional<User> user = userRepository.findById(doctorId);
		if (!user.isPresent()) {
			throw new UserException("Incorrect Doctor Id: " + doctorId);
		}
		List<Appointment> appointmentList = appointmentRepository.getAppointmentsFinishedByDoctor(doctorId);
		if (appointmentList.size()==0) {
			throw new NotFoundException("There is not any finished appointment....");
		}
		return appointmentList;
	}

	@Override
	public List<Appointment> getAppointmentsFinishedByDoctorUserName(String userName) throws NotFoundException {
		Optional<User> user = userRepository.getUserByEmail(userName);
		if (!user.isPresent()) {
			throw new UserException("Incorrect Doctor user Name: " + userName);
		}
		List<Appointment> appointmentList = appointmentRepository.getAppointmentsFinishedByDoctorUserName(userName);
		if (appointmentList.size()==0) {
			throw new NotFoundException("There is not any finished appointment....");
		}
		return appointmentList;
	}

	@Override
	public List<Appointment> getAppointmentsFinishedByCurrentUser(Authentication authentication) throws NotFoundException{
		
		List<Appointment> appointmentList = appointmentRepository.getAppointmentsFinishedByDoctorUserName(authentication.getName());
		if (appointmentList.size()==0) {
			throw new NotFoundException("You haven't finished any appointments...");
		}
		return appointmentList;
	}

	@Override
	public List<Appointment> getScheduledAppointmentsByDepartment(String department) throws NotFoundException {
		List<Appointment> appointmentList =  appointmentRepository.getScheduledAppointmentsByDepartment(department);
		if (appointmentList.size()==0) {
			throw new NotFoundException("There are haven't scheduled any appointments...");
		}
		return appointmentList;
	}

	@Override
	public List<Appointment> getAppointmentsByDepartment(String department) throws NotFoundException {
		List<Appointment> appointmentList =  appointmentRepository.getAppointmentsByDepartment(department);
		if (appointmentList.size()==0) {
			throw new NotFoundException("There are haven't scheduled any appointments...");
		}
		return appointmentList;
	}


	
}
