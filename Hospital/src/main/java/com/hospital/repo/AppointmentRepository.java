package com.hospital.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hospital.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	@Query("SELECT a FROM Appointment a JOIN a.patientInfo p WHERE p.mobile =:mobile")
	List<Appointment> findByPatientMobileNumber(String mobile);
	@Query("SELECT a FROM Appointment a WHERE a.id =:id")
	Optional<Appointment> findByAppointmentId(Long id);
	@Query("SELECT a FROM Appointment a JOIN a.patientInfo p WHERE p.mobile =:mobile AND p.aadhar =:aadhar")
	List<Appointment> findByPatientMobileAndAadhar(String mobile, String aadhar);
	
	@Query("SELECT a FROM Appointment a JOIN a.patientInfo p WHERE p.email =:email")
	List<Appointment> findByPatientEmail(String email);
	
	@Query("SELECT a FROM Appointment a JOIN a.patientInfo p WHERE p.id =:patientId")
	Optional<Appointment> findAppointmentByPatientId(Long patientId);
	
	@Query("SELECT a FROM Appointment a WHERE a.status = com.hospital.enums.AppointmentStatus.FINISHED")
	List<Appointment> findFinishedAppointments();
	@Query("SELECT a FROM Appointment a WHERE a.status = com.hospital.enums.AppointmentStatus.SCHEDULED")
	List<Appointment> findScheduledAppointments();
	@Query("SELECT a FROM Appointment a WHERE a.status = com.hospital.enums.AppointmentStatus.CANCELED")
	List<Appointment> findCanceledAppointments();
	@Query("SELECT a FROM Appointment a JOIN a.user u WHERE a.status = com.hospital.enums.AppointmentStatus.FINISHED AND u.id=:doctorId")
	List<Appointment> getAppointmentsFinishedByDoctor(String doctorId);
	@Query("SELECT a FROM Appointment a JOIN a.user u WHERE a.status = com.hospital.enums.AppointmentStatus.FINISHED AND u.id=:userId")
	List<Appointment> getAppointmentsFinishedByDoctorUserName(String userId);
	@Query("SELECT a FROM Appointment a JOIN a.slot s JOIN s.department d WHERE a.status = com.hospital.enums.AppointmentStatus.SCHEDULED AND d.name=:department")
	List<Appointment> getScheduledAppointmentsByDepartment(String department);
	@Query("SELECT a FROM Appointment a JOIN a.slot s JOIN s.department d WHERE (a.status = com.hospital.enums.AppointmentStatus.SCHEDULED OR a.status = com.hospital.enums.AppointmentStatus.FINISHED) AND d.name=:department")
	List<Appointment> getAppointmentsByDepartment(String department);


}
