package com.hospital.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.Category;
import com.hospital.model.User;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	public Optional<Category> findByName(String categoryName);
	

}
