
# 🧭 Service Registration and Discovery (Eureka)

## ✅ Purpose
To allow each microservice (like `auth-service`, `patient-service`, `doctor-service`) to **register with a central Eureka server** for **dynamic discovery and communication** without hardcoding URLs.

---

## 🔧 How It Works

Each service:
- Includes `spring-cloud-starter-netflix-eureka-client` as a dependency.
- Sets `@EnableEurekaClient` (or just `@SpringBootApplication` in Spring Boot 3+).
- Configures the Eureka server URL in `application.yml`.

The Eureka server:
- Is defined in the `discovery-server` module.
- Maintains a registry of all running services.
- Allows lookup by `service-name`.

---

## 📦 Key Components

| Component         | Description                                                               |
|------------------|---------------------------------------------------------------------------|
| **Eureka Server** | Central registry for microservices                                       |
| **Service Name**  | Each microservice registers with a name like `auth-service`, `patient-service` |
| **Feign Clients** | Use the service name to make calls via `@FeignClient("auth-service")`    |

---

## ✅ Sample Configurations

### 1. `discovery-server` (Eureka Server)

**`pom.xml` dependency:**
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

**Main Class:**
```java
@EnableEurekaServer
@SpringBootApplication
public class DiscoveryServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(DiscoveryServerApplication.class, args);
  }
}
```

**`application.yml`:**
```yaml
server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

---

### 2. Other Services (e.g., `auth-service`, `patient-service`)

**`pom.xml` dependency:**
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**Main Class:**
```java
@SpringBootApplication
@EnableEurekaClient // optional in Spring Boot 3+
public class AuthServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(AuthServiceApplication.class, args);
  }
}
```

**`application.yml`:**
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true

spring:
  application:
    name: auth-service
```

---

## ✅ Sample Feign Usage with Eureka

```java
@FeignClient(name = "auth-service", path = "/api/auth")
public interface AuthClient {
  @PostMapping("/token/verify")
  TokenValidationResponse validateToken(@RequestBody TokenValidationRequest request);
}
```
