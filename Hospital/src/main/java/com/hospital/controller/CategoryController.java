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

import com.hospital.model.Category;
import com.hospital.service.interfaces.CategoryService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired private CategoryService categoryService;
	
	@SecurityRequirement(name = "bearer-key")
	@PostMapping("/add")
	public ResponseEntity<Category> createCategory(@RequestBody Category category) {
		Category ct = categoryService.createCategory(category);
		return new ResponseEntity<Category>(ct , HttpStatus.CREATED);
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Category>> getAllCategories(){
		List<Category> ct = categoryService.getAllCategories();
		return new ResponseEntity<List<Category>>(ct , HttpStatus.OK);
	}
	
	@SecurityRequirement(name = "bearer-key")
	@GetMapping("/byName")
	public ResponseEntity<Category> getCategorieByName(@RequestParam String name){
		
		Category ct = categoryService.getCategoryByName(name);
		return new ResponseEntity<Category>(ct , HttpStatus.OK);
	}
	
	
}
