package com.hospital.service.interfaces;

import java.util.List;

import com.hospital.exception.NotFoundException;
import com.hospital.model.Contact;
import com.hospital.service.exception.ContactException;

public interface ContactService {
	
	 Contact createContact(Contact user) throws ContactException;
	 List<Contact> getAllContacts() throws NotFoundException;


}
