package com.ridoh.doctor_service.security;

import com.ridoh.doctor_service.client.AuthClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public ServiceAuthFilter serviceAuthFilter(AuthClient authClient) {
        return new ServiceAuthFilter(authClient);
    }
}
