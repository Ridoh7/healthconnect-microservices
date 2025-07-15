# 🩺 Patient Service (patient-service)

## ✅ Purpose
Handles operations related to patients, such as registration, updates, and management of patient records.

---

## 📦 Key Components

| Component         | Description                                      |
|------------------|--------------------------------------------------|
| `Patient` Entity  | Represents patient data in the database         |
| `PatientController` | Exposes REST endpoints for managing patients  |
| `PatientService`     | Business logic for patient operations         |
| `PatientRepository` | Data access layer for patients                 |
| `ServiceAuthFilter` | Secures routes by validating JWTs via auth-service |
| `AuthClient`        | Feign client for communicating with auth-service |

---

## 🚀 API Endpoints

| Method | Endpoint             | Description                  | Secured |
|--------|----------------------|------------------------------|---------|
| POST   | `/api/patient`       | Register a new patient       | ✅ Yes   |
| GET    | `/api/patient/{id}`  | Fetch a patient by ID        | ✅ Yes   |
| GET    | `/api/patient`       | Get all patients             | ✅ Yes   |

---

## 🔐 Security (via ServiceAuthFilter)
- Extracts Bearer token from `Authorization` header.
- Validates it by calling `auth-service` using Feign (`AuthClient`).
- Rejects unauthorized requests.

---

## 🛠 Sample Config

### `application.yml`
```yaml
spring:
  application:
    name: patient-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true

server:
  port: 8081
```

---

### 🧪 Feign Client (for token validation)
```java
@FeignClient(name = "auth-service", path = "/api/auth")
public interface AuthClient {
  @PostMapping("/token/verify")
  TokenValidationResponse validateToken(@RequestBody TokenValidationRequest request);
}
```

---

## 🧾 Notes
- Uses Lombok for DTOs and models.
- Depends on `common-lib` for shared classes like `Role`, `TokenValidationRequest`, `TokenValidationResponse`.
- Secured via `ServiceAuthFilter` applied in `SecurityConfig`.

---

## 🏁 Outcome
- Patient data is securely managed.
- Role-based access is enforced by verifying tokens against the centralized auth-service.