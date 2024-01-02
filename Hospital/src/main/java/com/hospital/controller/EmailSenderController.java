package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.service.EmailSendingServiceImpl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "/mail")
@SecurityRequirement(name = "bearer-key")
public class EmailSenderController {

	@Autowired EmailSendingServiceImpl emailService;

	
//	@GetMapping("/send")
//	@PreAuthorize("hasAuthority('ROLE_STAFF')")
//	public void testMail(@RequestParam String to) {
//		
//		Map<String, Object> map = new HashMap<>();
//		map.put("text", "hello I am ankit");
//		map.put("recipientName", "Vikash Kumar");
//		map.put("senderName", "Operation Team");
//		map.put("verificationLink", "https:localhost.com");
//
////		DomainOrder domainOrder = domainOrderService.findDomainOrderById("SHO1");
//		CcAvenueOrderStatus ccAvenueOrderStatus = ccAvenue.getPaymentStatus(null, "113009204272");
//		
//		try {
//			emailService.sendOrderSummary(domainOrder, ccAvenueOrderStatus);
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
	  
}
