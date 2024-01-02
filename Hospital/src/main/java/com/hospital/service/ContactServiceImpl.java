package com.hospital.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.exception.NotFoundException;
import com.hospital.model.Contact;
import com.hospital.repo.ContactRepository;
import com.hospital.service.exception.ContactException;
import com.hospital.service.interfaces.ContactService;


@Service
public class ContactServiceImpl implements ContactService{
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact createContact(Contact contact) {
    	if(contact==null || contact.getEmail()==null) {
    		throw new ContactException("No record filled...");
    	}
        return contactRepository.save(contact);
    }



    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts=  contactRepository.findAll();
        if(contacts.size()<=0) {
        	throw new NotFoundException("There are no contacts...");
        }
        return contacts;
    }

 


}
