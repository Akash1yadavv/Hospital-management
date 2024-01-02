package com.hospital.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.dto.SlotResponseDto;
import com.hospital.enums.SlotAvailabilityStatus;
import com.hospital.exception.NotFoundException;
import com.hospital.exception.SlotException;
import com.hospital.model.Department;
import com.hospital.model.Slot;
import com.hospital.util.SlotGenerator;
import com.hospital.repo.CategoryRepository;
import com.hospital.repo.DepartmentRepository;
import com.hospital.repo.SlotRepository;
import com.hospital.service.interfaces.SlotService;

@Service
public class SlotServiceImpl implements SlotService{

	@Autowired private  SlotRepository slotRepository;
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private DepartmentRepository departmentRepository;
//	@Autowired private SlotGenerator slotGenerator;;
	
	@Override
	public List<Slot> getBookedSlotList() throws SlotException {
		List<Slot> slots = slotRepository.getBookedSlots();
		if(slots.size()==0) {
			throw new SlotException("There are not any slots exit..");
		}
		return slots;
	}

	@Override
	public List<Slot> getBookedSlotListByDepartment( String department) throws SlotException {
		
		List<Slot> slots = slotRepository.getBookedSlotsByCategoryAndDepartmentForNext30Days(department.toUpperCase());
		
		if(slots.size()==0) {
			throw new SlotException("There are not any slots exit..");
		}
		
		return slots;
	}

	@Override
	public List<SlotResponseDto> getAvailableSlotsListByDepartment(String department)
			throws NotFoundException {
		SlotGenerator slotGenerator = new SlotGenerator();
		List<LocalDateTime> slots = slotGenerator.generateSlots();
		
		
		if(department==null) {
			throw new NotFoundException("department must not be null...");
		}
		Optional<Department> department2 = departmentRepository.findByName(department.toUpperCase());
		
		if(!department2.isPresent()) {
			throw new NotFoundException("Incorrect department name: " + department);
		}
		System.out.println("===================================");
		List<SlotResponseDto> slotList = new ArrayList<>();
		
		for(LocalDateTime slot2:slots) {
			Optional<Slot> slot = slotRepository.getBookedSlotsByDepartmentAndDateTime(department.toUpperCase(), slot2);
			
			if(!slot.isPresent()) {
				SlotResponseDto sl = new SlotResponseDto();
				sl.setStartTime(slot2);
				sl.setAvailabilityStatus(SlotAvailabilityStatus.AVAILABLE);
				sl.setDepartmentName(department);
				slotList.add(sl);
			}
		}
		return slotList;
	}

	@Override
	public Slot saveSlotsInDataBase(Slot slot) throws SlotException {
		
		return null;
	}
	

}
