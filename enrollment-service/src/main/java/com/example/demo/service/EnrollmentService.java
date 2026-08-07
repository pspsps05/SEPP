package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Enrollment;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.StudentRepository;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, 
                             StudentRepository studentRepository, 
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // 1. Enrol student
    public Enrollment enrolStudent(Long studentId, Long courseId) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Error: Student with ID " + studentId + " does not exist.");
        }

        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Error: Course with ID " + courseId + " does not exist.");
        }

        if (enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(studentId, courseId, "ACTIVE")) {
            throw new RuntimeException("Error: Student is already actively enrolled in this course.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setStatus("ACTIVE");
        enrollment.setEnrolledDate(LocalDate.now());

        return enrollmentRepository.save(enrollment);
    }

    // 2. View student's enrolments
    public List<Enrollment> getActiveEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentIdAndStatus(studentId, "ACTIVE");
    }

    // 3. Withdraw
    public Enrollment withdrawStudent(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseIdAndStatus(studentId, courseId, "ACTIVE")
                .orElseThrow(() -> new RuntimeException("Error: Student is not actively enrolled in this course."));

        enrollment.setStatus("DROPPED");

        // save
        return enrollmentRepository.save(enrollment);
        
    }
}