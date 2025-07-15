# 🔐 Auth Service (Authentication & Authorization)

## ✅ Purpose

The `auth-service` is responsible for user authentication, issuing and validating JWT tokens, and managing roles such as `ADMIN` and `GATEWAY`. Other services rely on it for verifying identity and access control.

---

## 🛠 Key Responsibilities

- User login with username and password.
- Issue JWT tokens on successful authentication.
- Verify token validity and extract role.
- Seed initial users (admin & gateway) on startup.
- Communicate with other services through Feign clients.

---

## 📦 Core Components

| Component               | Purpose                                      |
|------------------------|----------------------------------------------|
| `User` Entity          | Represents system users with username, password, and role |
| `Role` Enum            | Defines roles: `ADMIN`, `GATEWAY`            |
| `JwtService`           | Handles JWT creation and validation          |
| `AuthController`       | Exposes login, token validation endpoints     |
| `SecurityConfig`       | Configures Spring Security (password encoder, auth filter) |
| `CommandLineRunner`    | Initializes admin & gateway users at startup |

---

## 🔑 Endpoints

| Method | Path                     | Description                       |
|--------|--------------------------|-----------------------------------|
| POST   | `/api/auth/login`        | Authenticates a user and returns a JWT |
| POST   | `/api/auth/token/verify` | Validates a token and returns `username` and `role` |
| POST   | `/api/auth/validate`     | Simplified validation, returns only `username` and `valid` |

---

## 🔒 Sample JWT Validation Response

```json
{
  "valid": true,
  "username": "admin",
  "role": "ADMIN"
}
```

---

## 🔧 Configuration Highlights (`application.yml`)

```yaml
spring:
  application:
    name: auth-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

---

## 🧪 Sample User Seeder (via CommandLineRunner)

```java
@Bean
public CommandLineRunner init(UserRepository userRepository) {
    return args -> {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
    };
}
```

---

## 🔁 Used By

- **Patient Service**
- **Doctor Service**
- **API Gateway**

These services call `/api/auth/token/verify` to validate JWTs and identify roles.

---

## 🔗 Feign Client Sample Usage

```java
@FeignClient(name = "auth-service", path = "/api/auth")
public interface AuthClient {
    @PostMapping("/token/verify")
    TokenValidationResponse validateToken(@RequestBody TokenValidationRequest request);
}
```