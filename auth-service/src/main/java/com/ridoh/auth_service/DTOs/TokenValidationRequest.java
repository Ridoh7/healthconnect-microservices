package com.ridoh.auth_service.DTOs;

import lombok.Data;

@Data
public class TokenValidationRequest {
    private String token;
}
