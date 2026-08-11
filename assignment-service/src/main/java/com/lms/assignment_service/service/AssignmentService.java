package com.lms.assignment_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.assignment_service.dto.AssignmentRequest;
import com.lms.assignment_service.dto.HandinRequest;
import com.lms.assignment_service.model.Assignment;
import com.lms.assignment_service.repository.AssignmentRepository;
import com.lms.assignment_service.repository.EnrollmentAccessRepository;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentEventPublisher eventPublisher;
    private final EnrollmentAccessRepository enrollmentAccessRepository;

   public AssignmentService(
        AssignmentRepository assignmentRepository,
        AssignmentEventPublisher eventPublisher,
        EnrollmentAccessRepository enrollmentAccessRepository) {

        this.assignmentRepository = assignmentRepository;
        this.eventPublisher = eventPublisher;
        this.enrollmentAccessRepository = enrollmentAccessRepository;
    }

    // --- Instructor Functions ---

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<Assignment> getAssignmentsByCourse(Long courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }

    public Assignment createAssignment(AssignmentRequest request) {
        Assignment assignment = new Assignment(
                request.getTitle(),
                request.getDescription(),
                request.getCourseId(),
                request.getInstructorId(),
                request.getMaxMarks(),
                request.getDueDate()
        );
        Assignment saved = assignmentRepository.save(assignment);
        eventPublisher.publishAssignmentCreated(saved.getId(), saved.getTitle());
        return saved;
    }

    public Assignment updateAssignment(Long id, AssignmentRequest request) {
        Assignment existing = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setCourseId(request.getCourseId());
        existing.setInstructorId(request.getInstructorId());
        existing.setMaxMarks(request.getMaxMarks());
        existing.setDueDate(request.getDueDate());
        return assignmentRepository.save(existing);
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    // --- Student Functions ---

    public Assignment handinAssignment(HandinRequest request) {

        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Check whether the student is actively enrolled in the course
        boolean hasAccess =
                enrollmentAccessRepository
                        .existsByStudentIdAndCourseIdAndStatus(
                                request.getStudentId(),
                                assignment.getCourseId(),
                                "ACTIVE"
                        );

        if (!hasAccess) {
            throw new RuntimeException(
                    "Student is not actively enrolled in this course"
            );
        }

        // Check if already submitted
        if (assignment.getSubmittedByStudentId() != null) {
            throw new RuntimeException(
                    "Assignment already submitted by a student"
            );
        }

        assignment.setSubmittedByStudentId(request.getStudentId());
        assignment.setSubmissionText(request.getSubmissionText());
        assignment.setSubmittedAt(LocalDateTime.now());

        Assignment saved = assignmentRepository.save(assignment);

        eventPublisher.publishAssignmentSubmitted(
                saved.getId(),
                saved.getSubmittedByStudentId()
        );

        return saved;
    }

    public Assignment cancelHandin(Long assignmentId, Long studentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (assignment.getSubmittedByStudentId() == null) {
            throw new RuntimeException("No hand-in to cancel");
        }
        if (!assignment.getSubmittedByStudentId().equals(studentId)) {
            throw new RuntimeException("You are not the one who submitted this");
        }

        assignment.setSubmittedByStudentId(null);
        assignment.setSubmissionText(null);
        assignment.setSubmittedAt(null);
        assignment.setAwardedMarks(null); // clear any awarded marks too
        return assignmentRepository.save(assignment);
    }

    public Assignment gradeAssignment(Long assignmentId, Integer marks) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        if (assignment.getSubmittedByStudentId() == null) {
            throw new RuntimeException("Cannot grade: no submission yet");
        }
        assignment.setAwardedMarks(marks);
        return assignmentRepository.save(assignment);
    }
}