package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Enrollment;
import com.example.demo.service.EnrollmentService;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // 1. POST /enrollments - Enrol student
    @PostMapping
    public Enrollment enrolStudent(@RequestBody EnrollmentRequest request) {
        return enrollmentService.enrolStudent(request.getStudentId(), request.getCourseId());
    }

    // 2. GET /enrollments/student/{studentId} - View student's active enrolments
    @GetMapping("/student/{studentId}")
    public List<Enrollment> getActiveEnrollments(@PathVariable Long studentId) {
        return enrollmentService.getActiveEnrollments(studentId);
    }

    // 3. PUT /enrollments/withdraw - Withdraw student from a course
    @PutMapping("/withdraw")
    public Enrollment withdrawStudent(@RequestBody EnrollmentRequest request) {
        return enrollmentService.withdrawStudent(request.getStudentId(), request.getCourseId());
    }
}