package com.ridoh.patient_service.client;

import com.ridoh.auth_service.DTOs.AuthRequest;
import com.ridoh.auth_service.DTOs.AuthResponse;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class FeignConfig {

    private final AtomicReference<String> tokenHolder = new AtomicReference<>();

    @Bean
    public RequestInterceptor requestInterceptor(RestTemplate restTemplate) {
        return template -> {
            // Only fetch token if it's null or empty (you can later enhance this to refresh on expiry)
            if (tokenHolder.get() == null) {
                String token = fetchToken(restTemplate);
                tokenHolder.set(token);
            }

            template.header("Authorization", "Bearer " + tokenHolder.get());
        };
    }

    private String fetchToken(RestTemplate restTemplate) {
        String authUrl = "http://localhost:8083/api/auth/login";
        AuthRequest request = new AuthRequest("gateway", "gateway123");
        AuthResponse response = restTemplate.postForObject(authUrl, request, AuthResponse.class);

        if (response == null || response.getToken() == null) {
            throw new RuntimeException("Failed to fetch token from auth-service");
        }

        return response.getToken();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
