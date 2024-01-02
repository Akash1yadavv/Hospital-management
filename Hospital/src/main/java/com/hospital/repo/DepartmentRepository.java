package com.hospital.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hospital.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{
	
	public Optional<Department> findByName(String departmentName);
	@Query("SELECT DISTINCT d FROM Department d JOIN d.slots s WHERE s.startTime = :startTime")
    List<Department> findAllBySlotStartTime(LocalDateTime startTime);
	@Query("SELECT d.id, d.name FROM Department d JOIN d.category c WHERE  c.name=:categoryName")
	List<Object[]> findDepartmentByCategory(String categoryName);
	
	@Query("SELECT d FROM Department d JOIN d.users u WHERE u.id =:userName")
	List<Department> findDepartmentByUserName( String userName);


}
