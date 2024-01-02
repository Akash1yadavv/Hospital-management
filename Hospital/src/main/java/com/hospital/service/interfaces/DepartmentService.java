package com.hospital.service.interfaces;

import java.util.List;

import com.hospital.exception.CategoryException;
import com.hospital.exception.DepartmentException;
import com.hospital.exception.NotFoundException;
import com.hospital.model.Department;

public interface DepartmentService {

	public Department addDepartment(Department department, String category) throws DepartmentException;
	public Department updateDepartment(Department department) throws DepartmentException;
	public Department deleteDepartment(int departmentId) throws DepartmentException;
	public Department getDepartmentById(int departmentId) throws DepartmentException;
	public Department getDepartmentByName(String departmentName) throws NotFoundException;
	public List<Department> getAllDepartments() throws NotFoundException;
	public List<Department> getAllDepartmentsByCategory(String categoryName) throws NotFoundException;
	
	
}
