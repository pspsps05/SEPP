package com.lms.assignment_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Long courseId;          // references course in course-service
    private Long instructorId;      // who created it
    private Integer maxMarks;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;

    // Student hand-in details
    private Long submittedByStudentId;  // null if not submitted
    private LocalDateTime submittedAt;
    private String submissionText;
    private Integer awardedMarks;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors, Getters & Setters
    public Assignment() {}

    public Assignment(String title, String description, Long courseId, Long instructorId, 
                      Integer maxMarks, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.maxMarks = maxMarks;
        this.dueDate = dueDate;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getInstructorId() { return instructorId; }
    public void setInstructorId(Long instructorId) { this.instructorId = instructorId; }

    public Integer getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Integer maxMarks) { this.maxMarks = maxMarks; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getSubmittedByStudentId() { return submittedByStudentId; }
    public void setSubmittedByStudentId(Long submittedByStudentId) { this.submittedByStudentId = submittedByStudentId; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public String getSubmissionText() { return submissionText; }
    public void setSubmissionText(String submissionText) { this.submissionText = submissionText; }

    public Integer getAwardedMarks() { return awardedMarks; }
    public void setAwardedMarks(Integer awardedMarks) { this.awardedMarks = awardedMarks; }
}