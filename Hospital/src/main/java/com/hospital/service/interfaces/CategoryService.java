package com.hospital.service.interfaces;

import java.util.List;

import com.hospital.exception.AppointmentException;
import com.hospital.exception.CategoryException;
import com.hospital.model.Category;

public interface CategoryService {

	public Category createCategory(Category category) throws CategoryException;
	public List<Category> getAllCategories() throws CategoryException;
	public Category getCategoryById(int id) throws CategoryException;
	public Category updateCategory(Category category) throws CategoryException;
	public Category deleteCategory(int id) throws CategoryException;
	public Category getCategoryByName(String name) throws CategoryException;
	
	
}
