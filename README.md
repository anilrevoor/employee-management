# Employee Management Backend

## Overview

Employee Management is a Spring Boot REST API application used to manage employee information.

The backend provides employee CRUD operations, authentication, authorization, exception handling, centralized configuration, OAuth2 login, API versioning, logging, testing, and integration with the React frontend.

## Technology Stack

* Java 21
* Spring Boot 4.1.1
* Spring Data JPA
* H2 Database
* Spring Security
* JWT Authentication
* OAuth2 / Google Authentication
* Springdoc OpenAPI / Swagger
* JUnit 5
* Mockito
* JaCoCo
* Maven
* Spring Cloud Config Client

## Application URL

Backend:

`http://localhost:8080`

API base URL:

`http://localhost:8080/api/v1`

## Employee APIs

### Get All Employees

`GET http://localhost:8080/api/v1/employees`

### Get Employee By ID

`GET http://localhost:8080/api/v1/employees/{id}`

### Create Employee

`POST http://localhost:8080/api/v1/employees`

Example request:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "IT"
}
```

### Update Employee

`PUT http://localhost:8080/api/v1/employees/{id}`

### Delete Employee

`DELETE http://localhost:8080/api/v1/employees/{id}`

## API Versioning

The employee APIs use version 1:

`/api/v1/employees`

This provides a versioned API structure so future API versions can be introduced without breaking existing clients.

## JWT Authentication

Login endpoint:

`POST http://localhost:8080/api/v1/auth/login`

Example request:

```json
{
  "username": "admin",
  "password": "admin"
}
```

The login response contains:

* JWT token
* User role

Example:

```json
{
  "token": "<JWT_TOKEN>",
  "role": "ADMIN"
}
```

For protected APIs, send the token in the Authorization header:

```text
Authorization: Bearer <JWT_TOKEN>
```

The JWT token contains the user's role and has an expiry time.

## OAuth2 / Google Authentication

Google OAuth2 login URL:

`http://localhost:8080/oauth2/authorization/google`

After successful Google authentication, the application redirects the user to Swagger:

`http://localhost:8080/swagger-ui/index.html`

OAuth2 uses the following scopes:

* openid
* profile
* email

OAuth2 users are assigned the appropriate application role and are authenticated by Spring Security.

## Swagger

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

OpenAPI documentation:

`http://localhost:8080/v3/api-docs`

Swagger can be used to view and test the REST APIs.

## H2 Database

H2 Console:

`http://localhost:8080/h2-console`

JDBC URL:

`jdbc:h2:mem:employeedb`

The application uses H2 for the development/test database.

## Security

The application implements:

* JWT authentication
* OAuth2 authentication
* Role-based authorization
* USER and ADMIN roles
* Protected employee APIs
* Custom 401 Unauthorized handling
* 403 Forbidden handling

## Exception Handling

Global exception handling is implemented using `GlobalExceptionHandler`.

Employee not found scenarios use:

`EmployeeNotFoundException`

API errors are returned using `ApiErrorResponse`.

The response contains information such as:

* Timestamp
* HTTP status
* Error
* Message
* Request path

## Logging

SLF4J logging is used in the service layer.

The application logs important employee operations such as fetching employee information.

## Spring Cloud Config

The backend uses Spring Cloud Config for centralized configuration.

Application name:

`employee-management`

Active profile:

`dev`

Config Server:

`http://localhost:8888`

The backend can receive configuration from:

`employee-management-dev.properties`

Example configuration:

```properties
app.environment=development
logging.level.root=DEBUG
spring.jpa.show-sql=true
```

## Application Architecture

```text
                    +----------------------+
                    |   React Frontend     |
                    |   localhost:3000     |
                    +----------+-----------+
                               |
                               v
                    +----------------------+
                    | Spring Cloud Gateway |
                    |   localhost:8081     |
                    +----------+-----------+
                               |
                               v
                    +----------------------+
                    | Spring Boot Backend  |
                    |   localhost:8080     |
                    +----------+-----------+
                               |
                               v
                    +----------------------+
                    |     H2 Database       |
                    +----------------------+

                    Configuration
                           |
                           v
                    +----------------------+
                    | Spring Cloud Config  |
                    |   localhost:8888     |
                    +----------------------+
```

## Testing

The project uses:

* JUnit 5
* Mockito
* Spring Boot testing
* Integration testing
* JaCoCo

Run tests:

```bash
mvn clean test
```

Build the application:

```bash
mvn clean package
```

Build without running tests:

```bash
mvn clean package -DskipTests
```

## Test Coverage

JaCoCo is used to generate the test coverage report.

Report location:

```text
target/site/jacoco/index.html
```

The project includes unit tests for services, controllers, security components, and integration testing.

## Integration Testing

The integration test validates the complete backend flow including:

```text
Login
   |
   v
JWT Token
   |
   v
Create Employee
   |
   v
Retrieve Employee
```

This verifies that authentication and employee operations work together.

## Build Artifact

After running:

```bash
mvn clean package -DskipTests
```

the JAR is generated under:

```text
target/employee-management-0.0.1-SNAPSHOT.jar
```

`SNAPSHOT` indicates that the application version is a development version and not a final release version.

## CI/CD

The application can be integrated into a CI/CD pipeline.

Typical pipeline:

```text
Checkout Code
      |
      v
Build
      |
      v
Run Unit Tests
      |
      v
Run Integration Tests
      |
      v
Generate JaCoCo Report
      |
      v
Package JAR
      |
      v
Deploy
```

## Deployment

The application is prepared for deployment to cloud/container environments such as AWS and OpenShift.

## Important Ports

| Application                 | Port |
| --------------------------- | ---: |
| React Frontend              | 3000 |
| Spring Cloud Gateway        | 8081 |
| Employee Management Backend | 8080 |
| Config Server               | 8888 |

## Local Startup Order

Start the applications in the following order:

1. Employee Config Server - `8888`
2. Employee Management Backend - `8080`
3. Employee Management Gateway - `8081`
4. React Frontend - `3000`

## Project Purpose

This project demonstrates a complete enterprise-style employee management application using:

* Microservice architecture
* REST APIs
* API versioning
* JWT security
* OAuth2
* RBAC
* Centralized configuration
* API Gateway
* React frontend
* Redux Toolkit
* Automated testing
* Code coverage
* CI/CD readiness
* Cloud deployment readiness
