package com.ridoh.doctor_service.security;

import com.ridoh.doctor_service.client.AuthClient;
import com.ridoh.doctor_service.dto.TokenValidationRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceAuthFilter extends OncePerRequestFilter {
    private final AuthClient authClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        var validationRequest = new TokenValidationRequest();
        validationRequest.setToken(token);

        var result = authClient.validateToken(validationRequest);
        if (!result.isValid()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // ✅ Set authentication context with role
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + result.getRole()));
        var auth = new UsernamePasswordAuthenticationToken(result.getUsername(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        System.out.println("✅ Authenticated user: " + result.getUsername());
        System.out.println("✅ Role: " + result.getRole());


        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Allow health checks or internal routes without token
        String path = request.getServletPath();
        return path.startsWith("/actuator") || path.startsWith("/internal");
    }
}
