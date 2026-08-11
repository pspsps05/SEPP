package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentIdAndCourseIdAndStatus(
        Long studentId,
        Long courseId,
        String status
    );

    Optional<Enrollment> findByStudentIdAndCourseIdAndStatus(
        Long studentId,
        Long courseId,
        String status
    );

    List<Enrollment> findByStudentIdAndStatus(
        Long studentId,
        String status
    );
}