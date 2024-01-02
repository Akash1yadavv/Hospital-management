/**
 * 
 */
package com.hospital.dto.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author ankit
 */
@Data
public class EmailReqDto {
	@NotBlank @Email
	private String email;
}
