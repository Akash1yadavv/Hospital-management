package com.hospital.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{

}
