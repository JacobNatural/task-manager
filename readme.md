# Task Manager Application
![Static Badge](https://img.shields.io/badge/Build-passing-flat)
[![Static Badge](https://img.shields.io/badge/docs-blue)](https://jacobnatural.github.io/task-manager/index.html)

## ⚙️ Backend Overview
Task Manager is a reactive, lightweight backend system built with **Spring Boot** and **Spring WebFlux**, designed to manage users and their tasks efficiently. The application provides a **REST API** for creating, updating, retrieving, assigning, and deleting tasks and users. All endpoints return **reactive `Mono` responses**, ensuring non-blocking and scalable interactions.

The backend architecture focuses on **clean separation of concerns**, maintainability, and consistent error handling. It uses DTOs for request/response payloads and a **global exception handler** to return unified error messages wrapped in `ResponseDto`.

## 💻 Technology Stack

- Backend: Java, Spring Boot, Spring WebFlux, Reactor

- Database: MongoDB

- Build Tool: Maven

- Containerization: Docker

- Validation: Jakarta Validation (Bean Validation)

- API Documentation: Swagger/OpenAPI via Springdoc

## 🏁 Getting Started


### 📋 Prerequisites
- **Docker**

### 📥 Cloning the repository:
- To clone the repository and navigate into the project directory, run:

```bash
git clone https://github.com/JacobNatural/task-manager.git
cd task-manager 
```

### ▶️ Running the application:
- To build the application and run it using Docker Compose:
```bash
docker-compose up -d
```

### 🌐 Access to the Swagger UI:
```bash
http://localhost:8080/swagger-ui/index.html
```

### 🧪 Running Tests
- To execute the tests, ensure that your database configuration for tests is set up in
  the docker-compose-test.yml file and then run:
```bash 
mvn clean test
```

## 🚀 Key Features

### 👤 User Management
- Create, retrieve, update, and delete users
- Assign or remove tasks from a user
- Fetch user details with assigned tasks

### ✅ Task Management
- Create, update, and delete tasks
- Retrieve all tasks (optionally filtered by status or user)
- Supports pagination and sorting

### ⚠️ Error Handling
- Centralized global exception handler for validation errors, entity not found, input parsing, and generic server errors.

## 🤝 Contributing
We welcome contributions to improve the Shop Statistic Application. Here's how you can contribute:

1. Fork the repository on GitHub.
2. Make enhancements or fix issues in your forked copy.
3. Submit a pull request to merge your changes into the main repository.

Please ensure your code adheres to our coding standards and is thoroughly tested before submitting a pull request.


