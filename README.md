# MyGradHub - Auth Service

Welcome to the **MyGradHub Auth Service** project! This microservice is responsible for handling user authentication and registration. It is built using **Spring Boot**, follows a clean architecture, and is fully containerized with Docker. Continuous Integration is managed via **GitHub Actions**, and code quality is enforced through **SonarCloud**.

---

## ✨ Overview

This service provides:

- User registration and login
- JWT-based authentication
- API documentation via Swagger
- H2 database console for local development

Technologies used:

- Java 17 + Spring Boot
- Maven for build and dependency management
- H2 Database (dev)
- JWT (JSON Web Token)
- Docker for containerization
- GitHub Actions for CI/CD
- SonarCloud for code quality and static analysis

---

## 🚀 Setup & Running Locally

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose (if running containerized)

### Clone the repository

```bash
git clone https://github.com/zuamirgoliveira/mygradhub.git
cd mygradhub
```

### Run locally (Dev Mode)

```bash
cd mygradhub-auth
mvn spring-boot:run
```

### Run with Docker

```bash
docker pull zuamirooliveira/mygradhub-auth:latest

docker run -p 8081:8081 zuamirooliveira/mygradhub-auth:latest
```

---

## 🕹️ Local API Access

- Swagger UI: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- API Docs: [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)
- H2 Console: [http://localhost:8081/h2-console](http://localhost:8081/h2-console)

### Main Endpoints

| Method | Endpoint              | Description                 | Auth Required |
| ------ | --------------------- | --------------------------- | ------------- |
| POST   | `/api/v1/auth/signup` | Register a new user         | No            |
| POST   | `/api/v1/auth/login`  | Login and receive JWT token | No            |
| GET    | `/api/v1/users`       | List all users              | Yes (JWT)     |

---

## ⚙️ Architecture

- **Domain Driven Design (DDD)** approach with separation of concerns
- Follows layered architecture: `controller`, `service`, `repository`, and `model`
- Uses **@ControllerAdvice** for global exception handling
- Integrated validation with detailed error response mapping

---

## ♻️ Continuous Integration

CI/CD is handled by **GitHub Actions**:

- Compile and test with coverage
- Static code analysis (Checkstyle, OWASP Dependency Check)
- SonarCloud analysis
- Docker image build and push to DockerHub

### Useful Links

- GitHub Actions: [CI Pipeline](https://github.com/zuamirgoliveira/mygradhub/actions)
- SonarCloud Dashboard: [View Analysis](https://sonarcloud.io/summary/overall?id=com-mygradhub-mygradhub-auth\&branch=main)
- DockerHub: [Image Repository](https://hub.docker.com/r/zuamirooliveira/mygradhub-auth/tags)
- Trello Board: [Sprint Tracking](https://trello.com/b/fSosNXoX/kanban-my-grad-hub-sprint1)

---

## 🌐 Environment Variables

| Variable                 | Description                      |
| ------------------------ | -------------------------------- |
| `JWT_SECRET`             | Secret key for JWT signing       |
| `JWT_ISSUER`             | Token issuer                     |
| `SPRING_PROFILES_ACTIVE` | Active profile (e.g., `default`) |

Can be passed via `application.properties` or environment.

---

## 🚫 Security

- JWT authentication for protected routes
- Custom Spring Security filter for token validation
- Graceful handling of expired or invalid tokens

---

## 🚧 Development Notes

- Code coverage is enforced by SonarCloud (currently set at 80%)
- Swagger provides clear API contract for integration
- For contributing, follow the clean code practices and test your changes

---

## 🙌 Contributing

Feel free to fork the project and submit a pull request. For ideas and roadmap, check the [Trello board](https://trello.com/b/fSosNXoX/kanban-my-grad-hub-sprint1).

---

## 🏆 Author

**Zuamir Oliveira**\
[GitHub Profile](https://github.com/zuamirgoliveira)

"Comecei algo grande que ainda não terminou. O prazo pode ter acabado, mas  o desenvolvimento continua"

