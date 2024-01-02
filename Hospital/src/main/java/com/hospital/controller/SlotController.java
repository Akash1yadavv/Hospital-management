package com.hospital.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.SlotResponseDto;
import com.hospital.exception.SlotException;
import com.hospital.model.Category;
import com.hospital.model.Slot;
import com.hospital.service.interfaces.SlotService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/slots")
public class SlotController {

	@Autowired private SlotService slotService;
	
	@SecurityRequirement(name = "bearer-key")
	@GetMapping("/booked")
	public ResponseEntity<List<Slot>> getAllSlots(){
		List<Slot> slots = slotService.getBookedSlotList();
		return new ResponseEntity<List<Slot>>(slots , HttpStatus.OK);
	}
	@GetMapping("/available-slots")
	public ResponseEntity<List<SlotResponseDto>> getAllAvailableSlots(@RequestParam String department) throws SlotException{
		List<SlotResponseDto> slots = slotService.getAvailableSlotsListByDepartment(department);
		return new ResponseEntity<List<SlotResponseDto>>(slots , HttpStatus.OK);
	}
	@SecurityRequirement(name = "bearer-key")
	@GetMapping("/booked-slots-by-department")
	public ResponseEntity<List<Slot>> getBookedSlotsByDepartment(@RequestParam String department) throws SlotException{
		List<Slot> slots = slotService.getBookedSlotListByDepartment(department);
		return new ResponseEntity<List<Slot>>(slots , HttpStatus.OK);
	}
}
