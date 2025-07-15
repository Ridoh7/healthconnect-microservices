# 📡 Service-to-Service Communication (Feign Client)

## 🔹 Purpose
Enable secure, declarative HTTP communication between microservices using Spring Cloud OpenFeign.

## 🔹 How It Works
Microservices communicate by calling endpoints exposed by peer services using Feign Clients. Each service registers with the `eureka-server` for discovery, and Feign clients use the registered service name to route requests via Spring Cloud LoadBalancer.

## 🔹 Key Components

| Component         | Description                                                  |
|------------------|--------------------------------------------------------------|
| **Feign Client** | Interface annotated with `@FeignClient("service-name")`      |
| **DTO**          | Data Transfer Object shared via `common-lib` module          |
| **Security**     | JWT-based Auth via `Authorization: Bearer <token>` header    |

## 🔹 Sample Feign Client (from patient-service → auth-service)
```java
@FeignClient(name = "auth-service", path = "/api/auth")
public interface AuthClient {
    @PostMapping("/token/verify")
    TokenValidationResponse validateToken(@RequestBody TokenValidationRequest request);
}
```

## 🔹 Sample DTO (shared via `common-lib`)
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationRequest {
    private String token;
}
```

## 🔹 Filter Setup (Token Validation in patient-service)
```java
@Component
@RequiredArgsConstructor
public class ServiceAuthFilter extends OncePerRequestFilter {

    private final AuthClient authClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        TokenValidationResponse result = authClient.validateToken(new TokenValidationRequest(token));

        if (!result.isValid()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }
}
```

## 🔹 Eureka Configuration
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

## 🔹 Dependencies (pom.xml)
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

## ✅ How to Run

1. **Start Eureka Discovery Server**
```bash
cd discovery-server
./mvnw spring-boot:run
```

2. **Start Config Server**
```bash
cd config-server
./mvnw spring-boot:run
```

3. **Start Auth Service**
```bash
cd auth-service
./mvnw spring-boot:run
```

4. **Start Patient Service**
```bash
cd patient-service
./mvnw spring-boot:run
```

5. **Test Communication**
   - Make an authenticated request to `patient-service` with a valid token
   - Token is validated via Feign call to `auth-service`

---