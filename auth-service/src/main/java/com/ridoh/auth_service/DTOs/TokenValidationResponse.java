package com.ridoh.auth_service.DTOs;

import com.ridoh.auth_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {
    private boolean valid;
    private String username;
    private Role role;

}
