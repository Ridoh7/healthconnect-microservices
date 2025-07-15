# 🩺 Doctor Service

## ✅ Purpose
The Doctor Service handles all operations related to doctors in the HealthConnect microservices architecture. This includes doctor registration, profile management, and hospital affiliations.

---

## ⚙️ Key Features

- Doctor registration & profile management
- Role-based access control (authenticated via `auth-service`)
- Token validation using Feign client
- Centralized logging of doctor-related actions

---

## 🧱 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Feign Client
- Eureka Discovery Client

---

## 🛡️ Security Overview

- JWT-based security
- Requests intercepted by `ServiceAuthFilter`
- Tokens validated via `auth-service` through Feign Client

---

## 🔐 Authentication Flow

1. Every request to `/api/doctor/**` is intercepted by `ServiceAuthFilter`.
2. The token in the `Authorization` header is passed to the `auth-service` using Feign.
3. If valid, request proceeds. Otherwise, it's blocked with HTTP 401.

---

## ✨ Sample Endpoint

```http
POST /api/doctor/register
Authorization: Bearer <token>
Content-Type: application/json

{
  "fullName": "Dr. Ahmed Bello",
  "email": "ahmed@hospital.com",
  "phoneNumber": "08012345678",
  "hospitalName": "Ikeja General",
  "specialization": "Cardiology"
}
```

---

## 📁 Directory Structure

```
doctor-service/
├── client/               # Feign client for calling auth-service
├── controller/           # REST endpoints for doctor operations
├── dto/                  # Data Transfer Objects
├── entity/               # JPA Entities
├── repository/           # JPA Repositories
├── security/             # Filters and security configs
├── service/              # Business logic
└── DoctorServiceApplication.java
```

---

## 🧪 Example Role-based Logging

```java
System.out.println("✅ Authenticated: " + result.getUsername() + " with role: " + result.getRole());
```

---

## 🧠 Notes

- Role enum and Token DTOs are reused from `common-lib`.
- Feign client ensures dynamic routing via Eureka service discovery.