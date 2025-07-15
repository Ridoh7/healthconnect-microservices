package com.ridoh.auth_service.controller;

import com.ridoh.auth_service.DTOs.AuthRequest;
import com.ridoh.auth_service.DTOs.AuthResponse;
import com.ridoh.auth_service.model.User;
import com.ridoh.auth_service.service.AuthService;
import com.ridoh.auth_service.service.JwtService;
import com.ridoh.common.dto.TokenValidationRequest;
import com.ridoh.common.dto.TokenValidationResponse;
import com.ridoh.common.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = authService.authenticateAndReturnUser(request); // custom method to return User

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());

        String token = jwtService.generateToken(claims, user);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(Authentication authentication) {
        return ResponseEntity.ok(authentication.getPrincipal().toString());
    }

    @PostMapping("/token/verify")
    public TokenValidationResponse verifyToken(@RequestBody TokenValidationRequest request) {
        try {
            String username = jwtService.extractUsername(request.getToken());
            boolean isValid = jwtService.isTokenValid(request.getToken(), username);
            Role role = Role.valueOf(jwtService.extractRole(request.getToken())); // extract role from token
            return new TokenValidationResponse(isValid, username, role);
        } catch (Exception e) {
            return new TokenValidationResponse(false, null, null);
        }
    }

    @PostMapping("/validate")
    public TokenValidationResponse validateToken(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String username = jwtService.extractUsername(token);
            boolean isValid = jwtService.isTokenValid(token, username);
            Role role = Role.valueOf(jwtService.extractRole(token));
            return new TokenValidationResponse(isValid, username, role);
        } catch (Exception e) {
            return new TokenValidationResponse(false, null, null);
        }
    }

}
