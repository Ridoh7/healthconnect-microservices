# 🌐 API Gateway (Spring Cloud Gateway)

## ✅ Purpose
Acts as the single entry point for all client requests. It handles routing, filtering, and security (e.g., authentication and authorization) before requests reach internal microservices.

---

## 🔧 How It Works

- Clients send requests to the gateway (e.g., `/api/auth/**`, `/api/patient/**`).
- The gateway uses **route definitions** to forward requests to the appropriate service (`auth-service`, `patient-service`, etc.) based on path patterns.
- A **custom security filter** (`GatewayAuthFilter`) intercepts requests, extracts the JWT token, and validates it with `auth-service`.
- Unauthenticated or invalid tokens result in `401 Unauthorized` responses.

---

## 📦 Key Components

| Component            | Description                                                     |
|---------------------|-----------------------------------------------------------------|
| `RouteLocator`      | Defines route paths and maps them to registered services        |
| `GatewayAuthFilter` | Validates incoming JWT tokens by calling `auth-service`         |
| Eureka Discovery     | Uses service names (e.g., `auth-service`) for dynamic routing   |

---

## ✅ Dependencies

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

---

## 🧩 Sample Configuration

### `application.yml`
```yaml
spring:
  application:
    name: api-gateway

  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        - id: auth-service
          uri: lb://auth-service
          predicates:
            - Path=/api/auth/**

        - id: patient-service
          uri: lb://patient-service
          predicates:
            - Path=/api/patient/**

        - id: doctor-service
          uri: lb://doctor-service
          predicates:
            - Path=/api/doctor/**
```

---

## 🛡️ Security Filter Logic

### `GatewayAuthFilter.java`
```java
@Component
@RequiredArgsConstructor
public class GatewayAuthFilter extends OncePerRequestFilter {

    private final AuthClient authClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        TokenValidationRequest validationRequest = new TokenValidationRequest(token);
        TokenValidationResponse result = authClient.validateToken(validationRequest);

        if (!result.isValid()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        System.out.println("✅ Authenticated user: " + result.getUsername());
        filterChain.doFilter(request, response);
    }
}
```

---

## 📣 Notes
- Only `/api/auth/**` is public; all others require a valid token.
- Routes are dynamic and resolved using Eureka service discovery.