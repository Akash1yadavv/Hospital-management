package com.hospital.dto.auth;

import lombok.Data;

@Data
public class PasswordResetReq {
	String token;
	String newPassword;
}
