package com.lms.assignment_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.assignment_service.model.EnrollmentAccess;

@Repository
public interface EnrollmentAccessRepository extends JpaRepository<EnrollmentAccess, Long> {

    Optional<EnrollmentAccess> findByStudentIdAndCourseId(Long studentId, Long courseId);
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, String status);
}