
# 🛠 Centralized Configuration Management (Spring Cloud Config Server)

## 📌 Purpose
The Config Server provides a central place to manage external properties for applications across all environments. It simplifies environment-specific configurations and keeps services stateless.

---

## ⚙️ How It Works

- The `config-server` fetches configuration properties from a remote **Git repository**.
- All microservices (doctor-service, patient-service, auth-service, etc.) are configured as **config clients**.
- On startup, each service fetches its configuration from the `config-server`.
- This enables **centralized config management**, **version control**, and **dynamic config refresh**.

---

## 🧩 Key Components

| Component        | Description |
|------------------|-------------|
| **Config Server** | Acts as the centralized server to provide application configs. |
| **Git Repo**      | Stores the config files (e.g., `doctor-service.yml`, `patient-service.yml`). |
| **Bootstrap Config** | Each client service uses `bootstrap.yml` to connect to the config server. |

---

## 📁 Sample Directory Structure

```
healthconnect-config-repo/
│
├── doctor-service.yml
├── patient-service.yml
├── auth-service.yml
└── application.yml
```

---

## 🔐 Sample `bootstrap.yml` (used by microservice clients)

```yaml
spring:
  application:
    name: doctor-service
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: true
```

---

## 🧪 Test via Browser or Postman

To test if the config server is running:
```
GET http://localhost:8888/doctor-service/default
```

You should see the configuration pulled from the Git repo for the `doctor-service`.

---

## 🚨 Common Issues

- Ensure the Git repo is reachable (private/public with correct credentials).
- Check the application name matches the config file name (e.g., `auth-service.yml` for `auth-service`).
- Don't forget to use `bootstrap.yml` (not `application.yml`) for config clients.

---

## 🏁 Outcome

Once configured:
- All services automatically pull their configs.
- Changing values in the Git repo allows config refresh without redeploying services (when combined with Spring Cloud Bus).

---

### 📌 What does this mean?

> **"Changing values in the Git repo allows config refresh without redeploying services (when combined with Spring Cloud Bus)."**

This means that:

- Your **config-server** is connected to a central **Git-based configuration repository** (like `healthconnect-config-repo`).
- When you make changes to configuration files in that Git repo — for example:
  ```yaml
  # doctor-service.yml
  custom:
    feature-enabled: true
  ```
  — those new values are stored in the Git repo.

---

### 🧠 Normally (Without Spring Cloud Bus):

- After making that change, your services **won't know about it immediately**.
- You’d have to **restart each service manually** or call `/actuator/refresh` on each service individually to reload the config.

---

### 🚀 But *With* Spring Cloud Bus:

- When you call `/actuator/busrefresh` on **any one** service (e.g., doctor-service):
  ```bash
  POST http://localhost:8083/actuator/busrefresh
  ```
- The Spring Cloud Bus uses **RabbitMQ or Kafka** to broadcast a message to all other services.
- Each service then re-fetches its updated configuration from the config-server automatically.

✅ **No redeployment or manual restart is needed.**

---

### 🔁 So in short:
> Updating config values in Git → trigger a refresh → all services reload configs live → no restart needed (if Spring Cloud Bus is set up)