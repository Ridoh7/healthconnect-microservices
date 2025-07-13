package com.ridoh.auth_service;

import com.ridoh.auth_service.model.Role;
import com.ridoh.auth_service.model.User;
import com.ridoh.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@SpringBootApplication
public class AuthServiceApplication {

	private final PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole(Role.ADMIN);
				userRepository.save(admin);
				System.out.println("✅ Admin user created");
			}

			if (userRepository.findByUsername("gateway").isEmpty()) {
				User gateway = new User();
				gateway.setUsername("gateway");
				gateway.setPassword(passwordEncoder.encode("gateway123"));
				gateway.setRole(Role.GATEWAY);
				userRepository.save(gateway);
				System.out.println("✅ Gateway user created");
			}
		};
	}



	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
