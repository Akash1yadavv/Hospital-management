package com.hospital.dto.auth;

import lombok.Data;

@Data
public class LoginResDto {
    private String accessToken;
    private String type;
}
