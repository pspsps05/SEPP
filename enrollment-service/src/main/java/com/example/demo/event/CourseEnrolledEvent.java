package com.example.demo.event;

import java.time.LocalDate;

public class CourseEnrolledEvent {

    private Long studentId;
    private Long courseId;
    private String eventType;
    private LocalDate eventDate;

    public CourseEnrolledEvent() {
    }

    public CourseEnrolledEvent(Long studentId, Long courseId, String eventType, LocalDate eventDate) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.eventType = eventType;
        this.eventDate = eventDate;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }
}