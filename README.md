# Course Management System

## Overview

This project is a Course Management System developed with Java and Spring Boot for managing students, teachers, and courses.

The main focus of the project was backend development, with an emphasis on RESTful API design, CRUD operations, JPA entity relationships, database interaction, pagination, sorting, validation, exception handling, DTOs, and unit testing with Mockito.

A simple frontend was also developed using HTML, CSS, Vanilla JavaScript, and Bootstrap. The frontend was created entirely with the assistance of AI as a deliberate learning and experimentation approach to AI-assisted software development, rather than as a demonstration of frontend development expertise.

## Features

- Student management
- Teacher management
- Course management
- Student enrollment in courses
- Teacher assignment to courses
- View students enrolled in a course
- View courses of a student
- View courses taught by a teacher
- Pagination
- Sorting
- Bean Validation
- Exception Handling
- DTO-based API responses
- Unit testing with Mockito

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven

### Testing

- JUnit
- Mockito

### Frontend

- HTML
- CSS
- Vanilla JavaScript
- Bootstrap
- AI-assisted development

### Version Control

- Git
- GitHub

## Architecture

The backend follows a layered architecture:

- Controller Layer — Handles HTTP requests and responses.
- Service Layer — Contains business logic and application rules.
- Repository Layer — Handles database access through Spring Data JPA.
- Entity Layer — Represents the domain models and their relationships.

## Database & JPA Relationships

The project uses PostgreSQL as its relational database and JPA/Hibernate for object-relational mapping.

The main entities are:

- Student
- Teacher
- Course

The project includes the following relationships:

- One Teacher can teach many Courses.
- Each Course can have one Teacher.
- A Student can enroll in many Courses.
- A Course can have many Students.

The many-to-many relationship between Students and Courses is managed through a join table named `course_students`.

## API Endpoints

### Student

#### Endpoints

- `POST /students`
- `GET /students`
- `GET /students/{id}`
- `PUT /students/{id}`
- `DELETE /students/{id}`
- `GET /students/search`
- `GET /students/{studentId}/courses`

### Teacher

#### Endpoints

- `POST /teachers`
- `GET /teachers`
- `GET /teachers/{id}`
- `PUT /teachers/{id}`
- `DELETE /teachers/{id}`
- `GET /teachers/search`
- `GET /teachers/{teacherId}/courses`

### Course

#### Endpoints

- `POST /courses`
- `GET /courses`
- `GET /courses/{id}`
- `PUT /courses/{id}`
- `DELETE /courses/{id}`
- `GET /courses/search`
- `GET /courses/{courseId}/students`
- `POST /courses/{courseId}/{studentId}`
- `POST /courses/{courseId}/teacher/{teacherId}`

## Testing

The backend includes unit tests for the service layer using JUnit and Mockito.

The tests cover service behavior such as creating, retrieving, updating, and deleting entities, as well as handling different application scenarios and exceptions.

Mockito is used to mock repository dependencies and isolate the service layer during testing.

## AI-Assisted Frontend Development

The frontend of this project was developed entirely with the assistance of AI.

The frontend was intentionally built using HTML, CSS, Vanilla JavaScript, and Bootstrap as a learning and experimentation exercise in AI-assisted software development. The goal was to explore how AI can be used as a practical development tool for implementing, refining, and integrating a frontend with a backend REST API.

The frontend is not intended to represent professional frontend development expertise. The primary focus of this project remains backend development with Java and Spring Boot.

## Project Structure

```text
course-management/
├── backend/
│   └── src/
│       ├── main/
│       │   └── java/
│       │       └── com/shah/course_management/
│       │           ├── controller/
│       │           ├── dto/
│       │           ├── entity/
│       │           ├── exception/
│       │           ├── repository/
│       │           └── service/
│       │
│       └── test/
│           └── java/
│               └── com/shah/course_management/
│                   ├── CourseServiceTest.java
│                   ├── StudentServiceTest.java
│                   └── TeacherServiceTest.java
│
├── frontend/
└── README.md
```

## Getting Started

### Prerequisites

- Java 21
- Maven
- PostgreSQL
- Git

### Database Setup

Create a PostgreSQL database and configure the database connection in the backend application properties.

### Running the Backend

```bash
cd backend
mvn spring-boot:run
```

## API Examples

### Get All Students

```http
GET /students
```

### Enroll a Student in a Course

```http
POST /courses/{courseId}/{studentId}
```

### Assign a Teacher to a Course

```http
POST /courses/{courseId}/teacher/{teacherId}
```

## Future Improvements

- Authentication and authorization
- Spring Security
- JWT-based authentication
- Dockerization
- Integration testing
- File upload
- Redis caching
- WebSocket support
