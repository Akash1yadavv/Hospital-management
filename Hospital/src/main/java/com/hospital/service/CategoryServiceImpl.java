package com.hospital.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.exception.CategoryException;
import com.hospital.exception.NotFoundException;
import com.hospital.model.Category;
import com.hospital.repo.CategoryRepository;
import com.hospital.service.interfaces.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired private CategoryRepository categoryRepository;
	
	
	@Override
	public List<Category> getAllCategories() throws NotFoundException {
		List<Category> categories = categoryRepository.findAll();
		if(categories.size()==0) {
			throw new NotFoundException("No categories exist...");
		}
		return categories;
	}

	@Override
	public Category getCategoryById(int id) throws CategoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category createCategory(Category category) throws CategoryException {
		if(category == null) {
			throw new CategoryException("Category must not be null");
		}
		category.setName(category.getName().toUpperCase());
		
		categoryRepository.save(category);
		return category;
	}

	@Override
	public Category updateCategory(Category category) throws CategoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category deleteCategory(int id) throws CategoryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category getCategoryByName(String name) throws NotFoundException {
		Optional<Category> category = categoryRepository.findByName(name.toUpperCase());
		if(!category.isPresent()) {
			throw new NotFoundException("Category " + name + "is not present...");
		}
		return category.get();
	}

	

}
