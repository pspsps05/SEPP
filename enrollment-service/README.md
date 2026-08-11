# Enrollment Service

The Enrollment Service is one of the microservices in the Learning Management System (LMS).

It is responsible for managing student registration, course information, student course enrolment and course withdrawal. The service also publishes Kafka events when a student enrols in or withdraws from a course so that other microservices can react to enrolment changes.

---

## 1. Technologies Used

The Enrollment Service is developed using:

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Apache Kafka
- Maven

---

## 2. Main Functions

The Enrollment Service provides the following functions:

- Register students
- Create courses
- View students
- View courses
- Enrol a student in a course
- Prevent duplicate active enrolments
- View a student's active enrolments
- Withdraw a student from a course
- Publish `CourseEnrolledEvent`
- Publish `CourseDroppedEvent`
- Handle common errors using appropriate HTTP status codes

---

## 3. Service Configuration

### Enrollment Service

```text
Port: 8081
Base URL: http://localhost:8081
```
### H2 Database

```text
JDBC URL: jdbc:h2:mem:enrollmentdb
H2 Console: http://localhost:8081/h2-console
Username: sa
Password: Leave blank
```

> The H2 database is an in-memory database. Data will be cleared when the Enrollment Service is restarted.

### Kafka

```text
Bootstrap Server: localhost:9092
```

Kafka topics used by this service:

- `course-enrolled`
- `course-dropped`

---

## 4. REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/students` | Register a new student |
| GET | `/students` | View all students |
| POST | `/courses` | Create a new course |
| GET | `/courses` | View all courses |
| POST | `/enrollments` | Enrol a student in a course |
| GET | `/enrollments/student/{studentId}` | View a student's active enrolments |
| PUT | `/enrollments/withdraw` | Withdraw a student from a course |

---

## 5. Sample Requests

### Create Student

**POST** `/students`

```json
{
  "name": "Ali",
  "email": "ali@student.com"
}
```

### Create Course

**POST** `/courses`

```json
{
  "courseCode": "SEPP",
  "courseName": "Software Engineering Principles and Practices"
}
```

### Enrol Student

**POST** `/enrollments`

```json
{
  "studentId": 1,
  "courseId": 1
}
```

### Withdraw Student

**PUT** `/enrollments/withdraw`

```json
{
  "studentId": 1,
  "courseId": 1
}
```

---

## 6. Kafka Events

The Enrollment Service publishes events to Kafka when enrolment changes occur.

### Course Enrolled

Topic:

```text
course-enrolled
```

Example event:

```json
{
  "studentId": 1,
  "courseId": 1,
  "eventType": "COURSE_ENROLLED",
  "eventDate": "2026-08-11"
}
```

### Course Dropped

Topic:

```text
course-dropped
```

Example event:

```json
{
  "studentId": 1,
  "courseId": 1,
  "eventType": "COURSE_DROPPED",
  "eventDate": "2026-08-11"
}
```

These events can be consumed by other LMS microservices for event-driven communication.

---

## 7. Error Handling

The service handles common errors using appropriate HTTP status codes.

| Situation | HTTP Status |
|---|---|
| Student not found | `404 Not Found` |
| Course not found | `404 Not Found` |
| Active enrolment not found | `404 Not Found` |
| Duplicate active enrolment | `409 Conflict` |

---

## 8. Running the Service

### Step 1: Start Kafka

Make sure Apache Kafka is running on:

```text
localhost:9092
```

The following topics should exist:

```text
course-enrolled
course-dropped
```

To check the available Kafka topics:

```powershell
.\bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
```

If the topics do not exist, create them using:

```powershell
.\bin\windows\kafka-topics.bat --create --topic course-enrolled --bootstrap-server localhost:9092
```

```powershell
.\bin\windows\kafka-topics.bat --create --topic course-dropped --bootstrap-server localhost:9092
```

### Step 2: Start Enrollment Service

Open a terminal in the `enrollment-service` directory and run:

```powershell
.\mvnw.cmd spring-boot:run
```

The service will run at:

```text
http://localhost:8081
```

---

## 9. Testing Kafka Events

To monitor the `course-enrolled` topic:

```powershell
.\bin\windows\kafka-console-consumer.bat --topic course-enrolled --from-beginning --bootstrap-server localhost:9092
```

To monitor the `course-dropped` topic:

```powershell
.\bin\windows\kafka-console-consumer.bat --topic course-dropped --from-beginning --bootstrap-server localhost:9092
```

After a successful enrolment or withdrawal request, the corresponding Kafka event should appear in the consumer terminal.