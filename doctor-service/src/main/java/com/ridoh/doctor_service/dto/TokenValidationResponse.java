package com.ridoh.doctor_service.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class TokenValidationResponse {
    private boolean valid;
    private String username;
    private String role;

    // getters and setters
}
