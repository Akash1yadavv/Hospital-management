package com.hospital.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NameException extends RuntimeException{
	private NameExceptionDetails details;
}
