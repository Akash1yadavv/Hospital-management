package com.hospital.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.Appointment;
import com.hospital.model.Department;
import com.hospital.service.interfaces.DepartmentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

	@Autowired private DepartmentService departmentService;
	
	@SecurityRequirement(name = "bearer-key")
	@PostMapping("/add")
	public ResponseEntity<Department> addDepartment(@Valid @RequestBody Department department, @RequestParam String category) {
		Department dpt = departmentService.addDepartment(department, category);
		return new ResponseEntity<Department>(dpt , HttpStatus.CREATED);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Department>> GetAllDepartments(){
		List<Department> departments = departmentService.getAllDepartments();
		return new ResponseEntity<List<Department>>(departments , HttpStatus.OK);
	}
	@GetMapping("/by-category")
	public ResponseEntity<List<Department>> GetAllDepartmentsByCategory(@RequestParam String category){
		List<Department> departments = departmentService.getAllDepartmentsByCategory(category);
		return new ResponseEntity<List<Department>>(departments , HttpStatus.OK);
	}
	
}
