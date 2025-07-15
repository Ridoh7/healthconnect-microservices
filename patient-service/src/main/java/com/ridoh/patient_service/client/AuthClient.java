package com.ridoh.patient_service.client;


import com.ridoh.common.dto.TokenValidationRequest;
import com.ridoh.common.dto.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service",
        url = "http://localhost:8083", // or use service discovery if available
        configuration = FeignConfig.class
)
public interface AuthClient {

    @PostMapping("/api/auth/token/verify")
    TokenValidationResponse validateToken(@RequestBody TokenValidationRequest request);
}
