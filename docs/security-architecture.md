# 🔐 HealthConnect Microservices Security Architecture

This document outlines the JWT-based service-to-service authentication setup for the HealthConnect microservices architecture.

---

## 🔧 Overview

All internal microservices in the HealthConnect system authenticate requests using **JWT tokens issued by the `auth-service`**. Each secured endpoint validates tokens via the `auth-service`.

---

## 📌 Flow Summary

1. A service (e.g., `patient-service`) receives a request with a JWT.
2. The `ServiceAuthFilter` extracts the token and forwards it to `auth-service` via a Feign client.
3. If valid, the request proceeds. Otherwise, it is rejected with `401 Unauthorized`.

---

## 📤 Token Issuing (Login Endpoint)

- **URL**: `POST /api/auth/login`
- **Payload**:
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```
- **Response**:
  ```json
  {
    "token": "<JWT token>"
  }
  ```

---

## 🧪 Token Verification (Used by All Services)

Each service verifies tokens by calling:

```http
POST /api/auth/token/verify
Authorization: Bearer <JWT token>
Content-Type: application/json

{
  "token": "<JWT token>"
}
```

- **Valid Response**:
  ```json
  {
    "valid": true,
    "username": "admin",
    "role": "ADMIN"
  }
  ```

- **Invalid Response**:
  ```json
  {
    "valid": false
  }
  ```

---

## 👥 Feign Client Definition (Used by Each Service)

```java
@FeignClient(name = "auth-service")
public interface AuthClient {
    @PostMapping("/api/auth/token/verify")
    TokenValidationResponse validateToken(@RequestBody TokenValidationRequest request);
}
```

---

## 🔐 Security Filter in Services (`ServiceAuthFilter.java`)

```java
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
        var result = authClient.validateToken(new TokenValidationRequest(token));

        if (!result.isValid()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        System.out.printf("✅ Authenticated: %s with role: %s%n", result.getUsername(), result.getRole());

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/actuator") || path.startsWith("/internal");
    }
}
```

---

## 🛡️ Spring Security Config Example

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ServiceAuthFilter serviceAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/token/verify").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(serviceAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

---

## 📦 Shared DTOs (in `common-lib`)

### `TokenValidationRequest.java`
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationRequest {
    private String token;
}
```

### `TokenValidationResponse.java`
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationResponse {
    private boolean valid;
    private String username;
    private Role role;
}
```

### `Role.java`
```java
public enum Role {
    ADMIN,
    GATEWAY
}
```

---

## ✅ Tested Roles

| Username | Password   | Role     |
|----------|------------|----------|
| admin    | admin123   | ADMIN    |
| gateway  | gateway123 | GATEWAY  |

---

## 🧩 Notes

- Token verification is synchronous via Feign + Eureka discovery.
- Only verified tokens can access secured endpoints.
- Add new public endpoints in `.requestMatchers(...).permitAll()`.

---

© HealthConnect Microservices — 2025
