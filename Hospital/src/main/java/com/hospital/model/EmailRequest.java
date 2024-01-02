package com.hospital.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
	
	 @NotBlank
	 private String subject;
	 @NotBlank
	 private String text;

}
