package com.hospital.service.interfaces;

import java.util.List;

import com.hospital.dto.SlotResponseDto;
import com.hospital.exception.NotFoundException;
import com.hospital.exception.SlotException;
import com.hospital.model.Category;
import com.hospital.model.Slot;

public interface SlotService {

	public List<Slot> getBookedSlotList() throws NotFoundException;
	public List<Slot> getBookedSlotListByDepartment( String department) throws NotFoundException;
	public List<SlotResponseDto> getAvailableSlotsListByDepartment(String department) throws NotFoundException;
	Slot saveSlotsInDataBase(Slot slot) throws SlotException;
	
}
