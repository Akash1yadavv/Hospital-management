package com.hospital.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeEmailReqDto {
	@Email @NotBlank
	private String email;
}
