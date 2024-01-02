package com.hospital.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.exception.DepartmentException;
import com.hospital.exception.NotFoundException;
import com.hospital.model.Category;
import com.hospital.model.Department;
import com.hospital.repo.CategoryRepository;
import com.hospital.repo.DepartmentRepository;
import com.hospital.service.interfaces.DepartmentService;

@Service
public class DepartmentServiceImpl implements  DepartmentService{

	@Autowired private DepartmentRepository departmentRepository;
	@Autowired private CategoryRepository categoryRepository;
	
	@Override
	public Department addDepartment(Department department, String category) throws DepartmentException {
		
		if(department==null || category==null) {
			throw new DepartmentException("department or category cannot be null");
		}
		Optional<Category> ctg  = categoryRepository.findByName(category.toUpperCase());
		
		if(!ctg.isPresent()) {
			throw new DepartmentException("Invalid category name...");
		}
		
		department.setCategory(ctg.get());
		department.setName(department.getName().toUpperCase());
		departmentRepository.save(department);
		return department;
	}
	@Override
	public Department updateDepartment(Department department) throws DepartmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department deleteDepartment(int departmentId) throws DepartmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department getDepartmentById(int departmentId) throws DepartmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department getDepartmentByName(String departmentName) throws DepartmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Department> getAllDepartments() throws NotFoundException {
		List<Department> departments = departmentRepository.findAll();
		if(departments.size()==0) {
			throw new NotFoundException("There is not any departments existing...");
		}
		return departments;
	}
	@Override
	public List<Department> getAllDepartmentsByCategory(String categoryName) throws NotFoundException {
		List<Object[]> departmentData = departmentRepository.findDepartmentByCategory(categoryName.toUpperCase());
	    List<Department> departments = new ArrayList<>();
	    for (Object[] data : departmentData) {
	        Department department = new Department();
	        department.setId((Long) data[0]);
	        department.setName((String) data[1]);
	        departments.add(department);
	    }
		if (departments.size()==0) {
			throw new NotFoundException("There are not exist any departments");
		}
		return departments;
	}



	
}
