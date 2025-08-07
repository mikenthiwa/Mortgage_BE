# Mortgage Application Backend

This is a Spring Boot backend service for managing mortgage applications.

---

## Setup Guide

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Docker (optional, for containerized runs)

### Clone the Repository
```
git clone https://github.com/mikenthiwa/Mortgage_BE.git
cd mortgage
```

---

## How to Build & Run Locally

### 1. Build the Project
```
./mvnw clean install
```

### 2. Run the Application
```
./mvnw spring-boot:run
```
The backend will start on [http://localhost:8080](http://localhost:8080).

---

## How to Build & Run with Docker

### 1. Build Docker Image
```
docker build -t mortgage-app .
```

### 2. Run with Docker Compose
```
docker-compose up --build
```
This will start the backend and any dependent services defined in `docker-compose.yml`.

---

## Test Execution Instructions

### Run All Tests
```
./mvnw test
```

Test reports will be available in the `target/surefire-reports` directory.

---

## Frontend

If you have a deployed frontend (e.g., Netlify, Vercel, GitHub Pages), add the link here:

- [Frontend Deployment](https://your-frontend-link.com)

---

## Additional Resources
- [HELP.md](./HELP.md)
- [API Documentation] (if available)

---

For any issues, please open an issue or contact the maintainer.

