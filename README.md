## My Grad Hub (MGH) - Graduation Party Management
### 📌 Overview
**My Grad Hub** is a graduation party management application built with:
- Hexagonal Architecture
- Spring Boot 3.4.4
- Java 17
- H2 (in-memory for initial development)

###📦 Project Structure

mygradhub/
├── pom.xml                  # Parent POM  
├── mygradhub-auth/          # Authentication Service  
├── mygradhub-events/        # Events Service  
└── mygradhub-payments/      # Payments Service

### 🔧 Initial Setup
Prerequisites
- JDK 17
- Maven 3.8+
- IDE of your choice

How to Run
1. Clone the repository
2. Build the project:
```bash
mvn clean install
```
3. Start each service:
```bash
cd mygradhub-auth && mvn spring-boot:run  
cd ../mygradhub-events && mvn spring-boot:run  
cd ../mygradhub-payments && mvn spring-boot:run  
```
Local Access
- Auth Service: http://localhost:8081
- Events Service: http://localhost:8082
- Payments Service: http://localhost:8083
- H2 Consoles:
    - Auth: http://localhost:8081/h2-console
    - Events: http://localhost:8082/h2-console
    - Payments: http://localhost:8083/h2-console

### 📋 Product Backlog (Prioritized) - [Trello Kanban](https://trello.com/b/fSosNXoX/kanban-my-grad-hub-sprint1) 
#### Day 1: Foundations
- [MGH-001] Set up Maven multi-module structure
- [MGH-002] Implement JWT registration/login
- [MGH-003] Configure H2 in-memory
- [MGH-004] Document architecture
#### Day 2: Event Management
- [MGH-005] Create/edit events with basic data
- [MGH-006] Implement event registration
- [MGH-007] Configure service discovery
- [MGH-008] Add unit tests
#### Day 3: Payments
- [MGH-009] Implement payment flow
- [MGH-010] Process payment confirmations
- [MGH-011] Implement Saga Pattern
- [MGH-012] Configure centralized logs
#### Day 4: Frontend Foundations
- [MGH-013] Basic Angular frontend (Auth module)
- [MGH-014] Event listing frontend
#### Day 5: System Resilience
- [MGH-019] Add health checks and metrics
- [MGH-018] Implement circuit breaker
#### Day 6: Quality Assurance
- [MGH-021] Integration tests with Testcontainers
- [MGH-022] Code analysis with SonarQube
- [MGH-023] Validate end-to-end flows
#### Day 7: Documentation Finalization
- [MGH-024] Complete technical documentation
- [MGH-025] Justify architecture decisions

### 🛠️ Key Technologies
- Backend: Spring Boot 3.4.4, Hibernate, JWT
- Database: H2 (dev)
- Frontend: Angular (to be implemented)
- Architecture: Hexagonal + DDD

### 🌱 Next Steps
- Implement RabbitMQ for inter-service communication
- Add Eureka for service discovery
- Configure PostgreSQL for production
- Develop Angular frontend

✍️ My Grad Hub Team
