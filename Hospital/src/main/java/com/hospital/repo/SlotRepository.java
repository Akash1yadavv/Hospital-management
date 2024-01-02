package com.hospital.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hospital.model.Slot;

public interface SlotRepository extends JpaRepository<Slot, Long>{
//	@Query("SELECT s FROM Slot s JOIN s.category c JOIN s.department d WHERE c.name = :categoryName AND d.name = :departmentName AND s.startTime >= CURRENT_DATE + 1 ORDER BY s.startTime")
//	List<Slot> getBookedSlotsByCategoryAndDepartmentForNext30Days(String categoryName, String departmentName);
//	
	@Query("SELECT s FROM Slot s where s.availabilityStatus=BOOKED")
	List<Slot> getBookedSlots();
	
	@Query(value="SELECT s.id, s.start_time, s.end_time, s.availability_status, s.department_id, d.category_id\r\n"
			+ "FROM Slots s\r\n"
			+ "INNER JOIN Appointments a ON s.id = a.id_slot\r\n"
			+ "INNER JOIN Departments d ON d.id = s.department_id\r\n"
			+ "WHERE d.name =:departmentName AND s.start_time >= CURRENT_DATE + 1\r\n"
			+ "ORDER BY s.start_time;", nativeQuery = true)
	List<Slot> getBookedSlotsByCategoryAndDepartmentForNext30Days(String departmentName);
	
//	@Query(value="SELECT s.id, s.start_time, s.end_time, s.availability_status, s.department_id, d.category_id\r\n"
//			+ "FROM Slots s\r\n"
//			+ "INNER JOIN Appointments a ON s.id = a.id_slot\r\n"
//			+ "INNER JOIN Departments d ON d.id = s.department_id\r\n"
//			+ "WHERE d.name =:departmentName AND s.start_time =:startTime \r\n"
//			+ "ORDER BY s.start_time;", nativeQuery = true)
	@Query("SELECT s FROM Slot s JOIN s.department d WHERE d.name = :departmentName AND s.startTime = :startTime ORDER BY s.startTime")
	Optional<Slot>getBookedSlotsByDepartmentAndDateTime(String departmentName, LocalDateTime startTime);
	
}