package com.lms.assignment_service.controller;

import com.lms.assignment_service.dto.AssignmentRequest;
import com.lms.assignment_service.dto.HandinRequest;
import com.lms.assignment_service.model.Assignment;
import com.lms.assignment_service.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // ---- Instructor endpoints ----

    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    @GetMapping("/course/{courseId}")
    public List<Assignment> getAssignmentsByCourse(@PathVariable Long courseId) {
        return assignmentService.getAssignmentsByCourse(courseId);
    }

    @PostMapping
    public Assignment createAssignment(@RequestBody AssignmentRequest request) {
        return assignmentService.createAssignment(request);
    }

    @PutMapping("/{id}")
    public Assignment updateAssignment(@PathVariable Long id, @RequestBody AssignmentRequest request) {
        return assignmentService.updateAssignment(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Student endpoints ----

    @PostMapping("/handin")
    public Assignment handinAssignment(@RequestBody HandinRequest request) {
        return assignmentService.handinAssignment(request);
    }

    @DeleteMapping("/{assignmentId}/handin/{studentId}")
    public Assignment cancelHandin(@PathVariable Long assignmentId, @PathVariable Long studentId) {
        return assignmentService.cancelHandin(assignmentId, studentId);
    }

    // ---- Instructor grading ----

    @PutMapping("/{assignmentId}/grade")
    public Assignment gradeAssignment(@PathVariable Long assignmentId, @RequestParam Integer marks) {
        return assignmentService.gradeAssignment(assignmentId, marks);
    }
}