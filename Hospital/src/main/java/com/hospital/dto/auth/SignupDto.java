package com.hospital.dto.auth;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class SignupDto {
	@NotNull @NotBlank @NotEmpty
    private String name;
	@Email
	@NotNull
    private String email;
	@NotNull
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "The password should be of at least one uppercase letter, at least one digit, at least one special character, and a minimum length of 8 characters.")
    private String password;
	
	private String mobile;


}
