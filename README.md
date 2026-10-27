# Employee Management REST API

A Spring Boot REST API using Java 21, Spring Data JPA, validation, Swagger UI, JUnit 5 and Mockito.

## Prerequisites

- JDK 21
- Maven 3.9+ (or add the Maven Wrapper)


## 1. Run tests

```bash
mvn clean test
```

The unit tests use Mockito and do not need Docker. The integration tests use Testcontainers and require Docker to be running; they start a disposable PostgreSQL container.

To run only the unit tests:

```bash
mvn -Dtest=EmployeeServiceTest test
```

## 2. Start the API

```bash
mvn spring-boot:run
```

The API listens on `http://localhost:8080`.

Swagger UI: <http://localhost:8080/swagger-ui.html>  
OpenAPI JSON: <http://localhost:8080/v3/api-docs>  
Health endpoint: <http://localhost:8080/actuator/health>

## 3. Try the API

Create an employee:

```bash
curl -i -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ada",
    "lastName": "Lovelace",
    "email": "ada.lovelace@example.com",
    "department": "Engineering",
    "jobTitle": "Senior Software Engineer",
    "salary": 85000.00,
    "joiningDate": "2022-01-10"
  }'
```

List all employees:

```bash
curl http://localhost:8080/api/employees
```

Filter by department:

```bash
curl "http://localhost:8080/api/employees?department=Engineering"
```

Get one employee (replace `1` with the returned ID):

```bash
curl http://localhost:8080/api/employees/1
```

Update an employee:

```bash
curl -i -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ada",
    "lastName": "Lovelace",
    "email": "ada.lovelace@example.com",
    "department": "Platform",
    "jobTitle": "Lead Engineer",
    "salary": 95000.00,
    "joiningDate": "2022-01-10"
  }'
```

Delete an employee:

```bash
curl -i -X DELETE http://localhost:8080/api/employees/1
```

## HTTP response behaviour

- `201 Created`: employee created
- `200 OK`: employee/list returned or updated
- `204 No Content`: employee deleted
- `400 Bad Request`: invalid request fields
- `404 Not Found`: employee ID does not exist
- `409 Conflict`: duplicate email or database constraint conflict

## Test coverage included

### Unit tests (`EmployeeServiceTest`)
- Creates an employee and normalizes name/email
- Rejects a duplicate email
- Finds an existing employee
- Returns not-found for a missing employee
- Rejects an email already used by another employee during update
- Deletes an existing employee


