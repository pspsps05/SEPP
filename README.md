# Learning Management System - Microservices

This project implements a Learning Management System using a microservice and event-driven architecture.

## Microservices

| Service | Port | Main Responsibility |
|---|---:|---|
| Enrollment Service | 8081 | Student registration, course enrolment and withdrawal |
| Announcement Service | 8082 | Announcement and notification management |
| Assignment Service | 8083 | Assignment creation, submission, grading and enrolment validation |
| Course Material Service | 8084 | Course and learning material management |
| Apache Kafka | 9092 | Asynchronous inter-service communication |

## Project Structure

```text
enrollment-service/
announcement-service/
assignment-service/
course-service/