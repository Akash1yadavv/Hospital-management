package com.hospital.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PasswordResetRequestDto {
	@Email @NotNull @NotBlank @NotEmpty
	private String email;
}
