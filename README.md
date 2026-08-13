# Learning Management System (LMS) - Microservices

## 1. Project Overview

This project is a Learning Management System (LMS) developed using a microservice architecture. The system separates the main LMS business functions into four independent Spring Boot microservices and uses Apache Kafka for asynchronous event-driven communication between the services.

The system consists of the following microservices:

| Microservice                               | Port | Main Responsibility                                             |
| ------------------------------------------ | ---: | --------------------------------------------------------------- |
| Enrollment Service                         | 8081 | Student registration, course enrolment and course withdrawal    |
| Announcement Service                       | 8082 | Stores and retrieves announcements generated from system events |
| Assignment Service                         | 8083 | Assignment creation, submission and enrolment access validation |
| Course Material Service (`course-service`) | 8084 | Course and learning material management                         |
| Apache Kafka                               | 9092 | Asynchronous communication between microservices                |

---

## 2. Technologies Used

The project uses the following technologies:

* Java JDK 21 or above
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 Database
* Apache Kafka
* Maven / Maven Wrapper
* Postman
* Git and GitHub

---

## 3. Project Structure

```text
SEPP/
├── announcement-service/
├── assignment-service/
├── course-service/
├── enrollment-service/
└── README.md
```

Each microservice is implemented as an independent Spring Boot application and maintains its own H2 database.

---

## 4. Service Configuration

### 4.1 Enrollment Service

Port:

```text
8081
```

H2 Database:

```text
jdbc:h2:mem:enrollmentdb
```

H2 Console:

```text
http://localhost:8081/h2-console
```

Main responsibilities:

* Student management
* Course management for enrolment
* Course enrolment
* Course withdrawal
* Enrolment status management
* Publishing enrolment events

---

### 4.2 Announcement Service

Port:

```text
8082
```

H2 Database:

```text
jdbc:h2:mem:announcementdb
```

H2 Console:

```text
http://localhost:8082/h2-console
```

Main responsibilities:

* Store announcements
* Retrieve announcements
* Consume Kafka events from other microservices
* Generate notifications for relevant LMS events

---

### 4.3 Assignment Service

Port:

```text
8083
```

H2 Database:

```text
jdbc:h2:mem:assignmentdb
```

H2 Console:

```text
http://localhost:8083/h2-console
```

Main responsibilities:

* Create assignments
* Handle assignment submissions
* Maintain local enrolment access information
* Validate student enrolment before assignment submission
* Publish assignment events

---

### 4.4 Course Material Service

The Course Material Service is located in the `course-service` directory.

Port:

```text
8084
```

H2 Database:

```text
jdbc:h2:mem:coursedb
```

H2 Console:

```text
http://localhost:8084/h2-console
```

Main responsibilities:

* Course management
* Course material management
* Upload course materials
* Update course materials
* Retrieve materials by course
* Publish course and material events

---

### 4.5 H2 Console Login

For all H2 databases:

```text
Username: sa
Password: leave blank
```

Use the JDBC URL of the respective microservice when logging in.

---

## 5. Apache Kafka Configuration

Apache Kafka is used as the event broker for asynchronous communication between the microservices.

Kafka Broker:

```text
localhost:9092
```

The main Kafka topics used by the system include:

```text
course-enrolled
course-dropped
lms-events
course-events
material-events
```

Main event flows include:

```text
Enrollment Service
→ course-enrolled
→ Assignment Service
→ Announcement Service
```

```text
Enrollment Service
→ course-dropped
→ Assignment Service
→ Announcement Service
```

```text
Assignment Service
→ lms-events
→ Announcement Service
```

```text
Course Material Service
→ lms-events
→ Announcement Service
```

---

## 6. Running the System

### Step 1: Clone the Repository

Clone the final integration branch:

```bash
git clone -b final-integration https://github.com/pspsps05/SEPP.git
```

Enter the project folder:

```bash
cd SEPP
```

---

### Step 2: Verify Java

Check the installed Java version:

```bash
java -version
```

JDK 21 or above is required.

If `JAVA_HOME` is not configured, set it according to the location of the installed JDK.

Example for Windows PowerShell:

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-26.0.1"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

---

### Step 3: Start Apache Kafka

Open a separate PowerShell terminal and navigate to the Kafka installation folder:

```powershell
cd C:\kafka
```

Start the Kafka broker using the configured KRaft server properties file.

Example:

```powershell
.\bin\windows\kafka-server-start.bat .\config\kraft\server.properties
```

If a different KRaft configuration file is used in the local Kafka installation, replace `server.properties` with the configured file.

Kafka should be running on:

```text
localhost:9092
```

Keep the Kafka terminal running while using the system.

> Kafka storage does not need to be reformatted when restarting an existing Kafka installation.

---

### Step 4: Start the Enrollment Service

Open a new terminal:

```powershell
cd enrollment-service
.\mvnw.cmd spring-boot:run
```

Expected result:

```text
Tomcat started on port 8081
```

---

### Step 5: Start the Announcement Service

Open another terminal from the project root:

```powershell
cd announcement-service
.\mvnw.cmd spring-boot:run
```

Expected result:

```text
Tomcat started on port 8082
```

---

### Step 6: Start the Assignment Service

Open another terminal from the project root:

```powershell
cd assignment-service
.\mvnw.cmd spring-boot:run
```

Expected result:

```text
Tomcat started on port 8083
```

---

### Step 7: Start the Course Material Service

Open another terminal from the project root:

```powershell
cd course-service
.\mvnw.cmd spring-boot:run
```

Expected result:

```text
Tomcat started on port 8084
```

Apache Kafka and all four Spring Boot applications should remain running in separate terminals while testing the integrated system.

---

## 7. Sample API Requests

The REST APIs can be tested using Postman.

### 7.1 Create a Student

**Method:**

```text
POST
```

**URL:**

```text
http://localhost:8081/students
```

**Sample Request Body:**

```json
{
  "name": "Ali",
  "email": "ali@student.com"
}
```

---

### 7.2 Create a Course for Enrolment

**Method:**

```text
POST
```

**URL:**

```text
http://localhost:8081/courses
```

**Sample Request Body:**

```json
{
  "courseCode": "SEPP",
  "courseName": "Software Engineering Principles and Practices"
}
```

---

### 7.3 Enrol a Student in a Course

**Method:**

```text
POST
```

**URL:**

```text
http://localhost:8081/enrollments
```

**Sample Request Body:**

```json
{
  "studentId": 1,
  "courseId": 1
}
```

A successful enrolment:

* creates an enrolment with `ACTIVE` status;
* publishes a `CourseEnrolled` Kafka event;
* updates the Assignment Service local `EnrollmentAccess` record;
* creates an enrolment notification in the Announcement Service.

---

### 7.4 Retrieve Student Enrolments

**Method:**

```text
GET
```

**URL:**

```text
http://localhost:8081/enrollments/student/1
```

---

### 7.5 Withdraw from a Course

**Method:**

```text
PUT
```

**URL:**

```text
http://localhost:8081/enrollments/withdraw
```

**Sample Request Body:**

```json
{
  "studentId": 1,
  "courseId": 1
}
```

A successful withdrawal changes the enrolment status from:

```text
ACTIVE
```

to:

```text
DROPPED
```

The Enrollment Service then publishes a `CourseDropped` event through Apache Kafka.

---

## 8. Assignment Service Sample Requests

### 8.1 Create an Assignment

**Method:**

```text
POST
```

**URL:**

```text
http://localhost:8083/api/assignments
```

**Sample Request Body:**

```json
{
  "title": "Integration Assignment",
  "description": "Testing LMS integration.",
  "courseId": 1,
  "instructorId": 1,
  "maxMarks": 100,
  "dueDate": "2026-08-20T23:59:00"
}
```

---

### 8.2 Submit an Assignment

**Method:**

```text
POST
```

**URL:**

```text
http://localhost:8083/api/assignments/handin
```

**Sample Request Body:**

```json
{
  "assignmentId": 1,
  "studentId": 1,
  "submissionText": "Assignment submission."
}
```

If the student's local `EnrollmentAccess` status is:

```text
ACTIVE
```

the submission is accepted.

If the student's status is:

```text
DROPPED
```

the Assignment Service rejects the submission and returns:

```text
403 Forbidden
```

---

## 9. Announcement Service Sample Request

### Retrieve Announcements

**Method:**

```text
GET
```

**URL:**

```text
http://localhost:8082/api/announcements
```

The Announcement Service may contain notifications generated from events such as:

```text
COURSE_ENROLLED
COURSE_DROPPED
AssignmentCreated
AssignmentSubmitted
CourseCreated
MaterialUploaded
MaterialUpdated
```

---

## 10. Course Material Service Sample Requests

### 10.1 Create a Course

**Method:**

```text
POST
```

**URL:**

```text
http://localhost:8084/api/courses
```

**Sample Request Body:**

```json
{
  "courseCode": "SEPP",
  "courseName": "Software Engineering Principles and Practices",
  "description": "Microservices and event-driven architecture course.",
  "instructorName": "Dr Lim"
}
```

---

### 10.2 Upload Course Material

**Method:**

```text
POST
```

**URL:**

```text
http://localhost:8084/api/materials
```

**Sample Request Body:**

```json
{
  "courseId": 1,
  "title": "Week 5 Lecture Notes",
  "materialType": "PDF",
  "materialUrl": "/materials/week5.pdf"
}
```

A successful material upload publishes a `MaterialUploaded` event that can be consumed by the Announcement Service.

---

### 10.3 Update Course Material

**Method:**

```text
PUT
```

**URL:**

```text
http://localhost:8084/api/materials/1
```

**Sample Request Body:**

```json
{
  "courseId": 1,
  "title": "Week 5 Updated Lecture Notes",
  "materialType": "PDF",
  "materialUrl": "/materials/week5-updated.pdf"
}
```

A successful update publishes a `MaterialUpdated` event.

---

### 10.4 Retrieve Materials by Course

**Method:**

```text
GET
```

**URL:**

```text
http://localhost:8084/api/materials/course/1
```

---

## 11. Example Integration Flow

### Student Enrolment

```text
Student
   ↓
Enrollment Service
   ↓
Enrollment Status = ACTIVE
   ↓
CourseEnrolled Event
   ↓
Apache Kafka
   ├──────────────→ Assignment Service
   │                  ↓
   │             EnrollmentAccess
   │                 = ACTIVE
   │
   └──────────────→ Announcement Service
                      ↓
                Enrolment Notification
```

---

### Course Withdrawal

```text
Student Withdraws
      ↓
Enrollment Service
      ↓
Enrollment Status = DROPPED
      ↓
CourseDropped Event
      ↓
Apache Kafka
      ↓
Assignment Service
      ↓
EnrollmentAccess = DROPPED
      ↓
Student Attempts Assignment Submission
      ↓
403 Forbidden
```

This demonstrates that the microservices can exchange data through Apache Kafka without directly sharing their databases.

---

## 12. Testing the Assignment Integration

The Assignment Service H2 Console can be used to verify enrolment access.

Open:

```text
http://localhost:8083/h2-console
```

Use:

```text
JDBC URL: jdbc:h2:mem:assignmentdb
Username: sa
Password: leave blank
```

Run:

```sql
SELECT * FROM ENROLLMENT_ACCESS;
```

After a successful course enrolment, the expected status is:

```text
ACTIVE
```

After a successful course withdrawal, the expected status is:

```text
DROPPED
```

A student with `DROPPED` status should receive `403 Forbidden` when attempting to submit an assignment.

---

## 13. Notes

* H2 is configured as an in-memory database, therefore stored data is cleared when a microservice is restarted.
* Apache Kafka must be running before event-driven integration can be tested.
* Each microservice owns its own database.
* Microservices do not directly access databases owned by other services.
* Kafka events are used to propagate relevant changes between services.
* Each Spring Boot service should be run in a separate terminal.
* The Course Material Service is stored in the `course-service` directory.

---

## 14. Repository

GitHub Repository:

```text
https://github.com/pspsps05/SEPP
```

Final Integrated Branch:

```text
final-integration
```

Direct Link to Final Integrated Version:

```text
https://github.com/pspsps05/SEPP/tree/final-integration
```
