# EmployeeHub

EmployeeHub is a web-based Employee Management System built using Spring Boot. It allows users to manage employee information through a simple and responsive interface.

## Features

- Add new employees
- View employee details
- Edit employee information
- Delete employees
- Search employees by name or email
- Filter employees by department
- Pagination for employee records
- Employee dashboard
- Total employee count
- Department-wise employee statistics
- Recent employees section
- Responsive user interface

## Technologies Used

### Backend
- Java
- Spring Boot
- Spring Data JPA
- Hibernate

### Frontend
- HTML
- CSS
- Bootstrap
- Thymeleaf

### Database
- MySQL

### Tools
- IntelliJ IDEA
- Git
- GitHub
- Maven

## Project Structure

```text
employee-management-system
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.kowshik.ems
│   │   │       ├── controller
│   │   │       ├── model
│   │   │       ├── repository
│   │   │       ├── service
│   │   │       └── EmployeeManagementSystemApplication.java
│   │   │
│   │   └── resources
│   │       ├── static
│   │       ├── templates
│   │       └── application.properties
│
├── pom.xml
└── README.md
```

## Setup and Installation

### 1. Clone the repository

```bash
git clone https://github.com/Koushik13579/EmployeeHub.git
```
### 2. Open the project

Open the project in IntelliJ IDEA.

### 3. Create the MySQL database
CREATE DATABASE employee_management;
### 4. Configure the database

Update the database configuration in:

src/main/resources/application.properties

Example:

spring.datasource.url=jdbc:mysql://localhost:3306/employee_management
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

Set the DB_PASSWORD environment variable with your MySQL password.

### 5. Run the application

Run the Spring Boot application and open:
http://localhost:8080

## Screens

### The application includes:

Employee Dashboard

Employee List

Add Employee

Edit Employee

Employee Details

Future Improvements

User authentication and authorization

Role-based access control

Employee profile images

Advanced reporting

REST API support

## Author

Kowshik
