package com.ridoh.auth_service.service;

import com.ridoh.auth_service.DTOs.AuthRequest;
import com.ridoh.auth_service.DTOs.AuthResponse;
import com.ridoh.auth_service.DTOs.TokenValidationRequest;
import com.ridoh.auth_service.DTOs.TokenValidationResponse;
import com.ridoh.auth_service.model.User;
import com.ridoh.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 🔹 Inject it here

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    @Override
    public TokenValidationResponse validateToken(TokenValidationRequest request) {
        String token = request.getToken();
        String username;

        try {
            username = jwtService.extractUsername(token);
        } catch (Exception e) {
            return new TokenValidationResponse(false, null, null);
        }

        boolean isServiceToken = jwtService.isServiceToken(token);
        boolean isValid = isServiceToken;

        if (!isServiceToken) {
            isValid = userRepository.findByUsername(username)
                    .filter(user -> jwtService.isTokenValid(token, user.getUsername()))
                    .isPresent();
        }

        return new TokenValidationResponse(isValid, isValid ? username : null, null);
    }

    public User authenticateAndReturnUser(AuthRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return user;
    }


}
