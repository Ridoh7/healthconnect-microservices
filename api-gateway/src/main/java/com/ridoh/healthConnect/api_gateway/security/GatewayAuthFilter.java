package com.ridoh.healthConnect.api_gateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GatewayAuthFilter extends OncePerRequestFilter {

    private final ServiceTokenManager serviceTokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Attach token to all downstream requests via header (can be picked by RestTemplate interceptor)
        String serviceToken = serviceTokenManager.getServiceToken();

        // Optionally: log it once for debugging
        // System.out.println("🔐 Gateway attached service token: " + serviceToken);

        // Store in request attribute (if you want to pass it around)
        request.setAttribute("SERVICE_TOKEN", serviceToken);

        filterChain.doFilter(request, response);
    }
}
