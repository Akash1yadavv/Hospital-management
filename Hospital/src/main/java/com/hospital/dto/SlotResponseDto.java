package com.hospital.dto;

import java.time.LocalDateTime;

import com.hospital.enums.SlotAvailabilityStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotResponseDto {
	
    private LocalDateTime startTime;

    @Enumerated(EnumType.STRING)
    private SlotAvailabilityStatus availabilityStatus;
    
    private String departmentName;
}
