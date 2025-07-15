package com.ridoh.auth_service.service;

import com.ridoh.auth_service.DTOs.AuthRequest;
import com.ridoh.auth_service.DTOs.AuthResponse;
import com.ridoh.auth_service.model.User;
import com.ridoh.common.dto.TokenValidationRequest;
import com.ridoh.common.dto.TokenValidationResponse;

public interface AuthService {

    AuthResponse authenticate(AuthRequest request);
    TokenValidationResponse validateToken(TokenValidationRequest request);
    User authenticateAndReturnUser(AuthRequest request);

}
