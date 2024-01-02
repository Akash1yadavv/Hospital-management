/**
 * 
 */
package com.hospital.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hospital.dto.AppointmentRequestDto;
import com.hospital.dto.SlotResponseDto;
import com.hospital.dto.admin.panel.AdminPanelStatistics;
import com.hospital.dto.admin.panel.UserStatisticsData;
import com.hospital.dto.user.CurrentUserResDto;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.SlotAvailabilityStatus;
import com.hospital.exception.AppointmentException;
import com.hospital.exception.NotFoundException;
import com.hospital.exception.SlotException;
import com.hospital.exception.UserException;
import com.hospital.model.Appointment;
import com.hospital.model.Department;
import com.hospital.model.Slot;
import com.hospital.model.User;
import com.hospital.repo.DepartmentRepository;
import com.hospital.repo.SlotRepository;
import com.hospital.repo.UserRepository;
import com.hospital.service.interfaces.AdminPanel;
import com.hospital.service.interfaces.AppointmentService;
import com.hospital.service.interfaces.UserService;
import com.hospital.util.SlotGenerator;

/**
 * @author Akash Yadav
 */
@Service
public class AdminPanelImpl implements AdminPanel{

	@Autowired UserService userService;
	@Autowired UserRepository userRepository;
	@Autowired AppointmentService appointmentService;
	@Autowired SlotRepository slotRepository;
	@Autowired DepartmentRepository departmentRepository;
	
	
	
	@Override
	public AdminPanelStatistics getAdminPanelStatistics( Instant sincInstant, Instant tillInstant) {
		Long noRegisteredUsers = userService.countByCreatedOnLessThanAndCreatedOnGreaterThan(sincInstant, tillInstant);
		AdminPanelStatistics adminPanelStatistics = new AdminPanelStatistics();
		
		UserStatisticsData userStatisticsData = new UserStatisticsData();
		adminPanelStatistics.setUserStatisticsData(userStatisticsData);
		userStatisticsData.setNoRegisteredUsers(noRegisteredUsers);
		
		
		return adminPanelStatistics;
	}
	
//	*****************************************************SUPER ADMIN PANEL************************************************
	
	
	@Override
	public User registerDocter(User user) throws UserException {
		
		return null;
	}
	@Override
	public User deltetDocterByID(String id) throws UserException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User updateUser(User user) throws UserException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User getUserByUserName(User user) throws UserException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<User> getAllUser() throws NotFoundException {
		List<User> userList = userRepository.findAll();
		if (userList.size() == 0) {
			throw new NotFoundException("There are no users...");
		}
		
		return userList;
	}
	@Override
	public User getUserByUserId(String id) throws NotFoundException {
		Optional<User> user = userRepository.findById(id);
		if(user.isEmpty()) {
			throw new NotFoundException("User not found...");
			
		}
		return user.get();
	}
	@Override
	public User getUserByUserEmail(String email) throws NotFoundException {
		Optional<User> user = userRepository.getUserByEmail(email);
		if(user.isEmpty()) {
			throw new NotFoundException("User not found...");
			
		}
		return user.get();
	}
	@Override
	public List<Appointment> getAllAppointment() throws NotFoundException {
		List<Appointment> list = appointmentService.getAppointments();
		if(list.size()==0) {
			throw new NotFoundException("there are no appointment...");
		}
		return list;
	}
	@Override
	public List<Appointment> getResolvedAppointments() throws NotFoundException {
		List<Appointment> list = appointmentService.getResolvedAppointments();
		return list;
	}
	@Override
	public List<Appointment> getScheduledAppointments() throws NotFoundException {
		List<Appointment> list = appointmentService.getScheduledAppointments();
		return list;
	}
	@Override
	public List<Appointment> getCanceledAppointments() throws NotFoundException {
		List<Appointment> list = appointmentService.getCanceledAppointments();
		return list;
	}
	@Override
	public List<Appointment> getAppointmentsFinishedByDoctor(String Id) throws NotFoundException {
		List<Appointment> list = appointmentService.getAppointmentsFinishedByDoctor(Id);
		return list;
	}
	@Override
	public List<Appointment> getAppointmentsFinishedByDoctorUserName(String doctorUserName)
			throws NotFoundException {
		List<Appointment> list = appointmentService.getAppointmentsFinishedByDoctorUserName(doctorUserName);
		return list;
	}
	
	
//	***********************************************ADMIN SERVICES ********************************************
	
	
	@Override
	public Appointment updateAppointmentStatus(Authentication authentication, Long appointmentId, AppointmentStatus status) throws AppointmentException {
		Appointment appointment = appointmentService.updateAppointmentStatus(authentication,appointmentId,status);
		return appointment;
	}
	@Override
	public Appointment updateAppointment(AppointmentRequestDto appointmentRequestDto) throws AppointmentException {
		Appointment appointment = appointmentService.updateAppointment(appointmentRequestDto);
		return appointment;
	}
	@Override
	public List<Appointment> getAppointmentsFinishedByCurrentUser(Authentication authentication) throws NotFoundException {
		List<Appointment> appointments = appointmentService.getAppointmentsFinishedByCurrentUser(authentication);
		return appointments;
	}

	@Override
	public List<Appointment> getScheduledAppointmentsByDepartment(String department) throws NotFoundException {
		List<Appointment> appointments = appointmentService.getScheduledAppointmentsByDepartment(department.toUpperCase());
		return appointments;
	}
	@Override
	public List<Appointment> getAppointmentsByDepartment(String department) throws NotFoundException {
		List<Appointment> appointments = appointmentService.getAppointmentsByDepartment(department.toUpperCase());
		return appointments;
	}

	@Override
	public List<Slot> getLeave(Authentication authentication, LocalDate date) throws NotFoundException {
		SlotGenerator slotGenerator = new SlotGenerator();
		System.out.println("--------------------------------");
		List<LocalDateTime> slots = slotGenerator.generateSlots();
		
		List<Department> departments = departmentRepository.findDepartmentByUserName(authentication.getName());
		System.out.println("========================================");
		if(departments.size()==0) {
			throw new NotFoundException("coudn't find department by user id "+authentication.getName()+" ...");
		}
		
		
		List<Slot> slotList = new ArrayList<>();
		
		for(int i=0; i<departments.size(); i++) {
			for(LocalDateTime slot2:slots) {
				Optional<Slot> slot = slotRepository.getBookedSlotsByDepartmentAndDateTime(departments.get(i).getName(), slot2);
				if(!slot.isPresent() && slot2.toString().substring(0, 10).equals(date.toString())) {
					Slot sl = new Slot();
					sl.setStartTime(slot2);
					sl.setAvailabilityStatus(SlotAvailabilityStatus.ON_LEAVE);
					sl.setDepartment(departments.get(i));
					slotRepository.save(sl);
					slotList.add(sl);
				}
			}
		}
		return slotList;
	}


	
}
