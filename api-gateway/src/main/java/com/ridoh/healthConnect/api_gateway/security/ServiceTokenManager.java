package com.ridoh.healthConnect.api_gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ServiceTokenManager {

    private final RestTemplate restTemplate;

    private String token;

    private Instant expiryTime;

    public String getServiceToken() {
        if (token == null || Instant.now().isAfter(expiryTime)) {
            fetchTokenFromAuthService();
        }
        return token;
    }


    private void fetchTokenFromAuthService() {
        String url = "http://auth-service/api/auth/login";

        Map<String, String> request = new HashMap<>();
        request.put("username", "gateway");
        request.put("password", "gateway123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, httpEntity, Map.class);
            token = (String) response.getBody().get("token");
            System.out.println("🔐 New service token fetched from auth-service");
        } catch (Exception e) {
            System.err.println("❌ Failed to fetch service token: " + e.getMessage());
        }
    }
}
