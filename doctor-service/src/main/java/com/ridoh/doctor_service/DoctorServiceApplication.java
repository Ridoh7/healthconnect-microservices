package com.ridoh.doctor_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableMethodSecurity
@EnableFeignClients(basePackages = "com.ridoh.doctor_service.client")
public class DoctorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorServiceApplication.class, args);
	}

}
