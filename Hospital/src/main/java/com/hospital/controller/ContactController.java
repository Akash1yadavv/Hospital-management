package com.hospital.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.Contact;
import com.hospital.service.interfaces.ContactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contacts")
public class ContactController {
	
	@Autowired private ContactService  contactService;

    @PostMapping("/add")
    public ResponseEntity<Contact> createContact(@Valid @RequestBody Contact contact){
    	Contact savedContact = contactService.createContact(contact);
        return new ResponseEntity<Contact>(savedContact, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Contact>> getAllContacts(){
        List<Contact> contacts = contactService.getAllContacts();
        return new ResponseEntity<List<Contact>>(contacts, HttpStatus.OK);
    }


}
