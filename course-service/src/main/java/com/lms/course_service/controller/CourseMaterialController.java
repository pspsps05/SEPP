package com.lms.course_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.course_service.dto.CourseMaterialRequest;
import com.lms.course_service.model.CourseMaterial;
import com.lms.course_service.service.CourseMaterialService;

@RestController
@RequestMapping("/api/materials")
public class CourseMaterialController {

    private final CourseMaterialService courseMaterialService;

    // Constructor Injection
    public CourseMaterialController(CourseMaterialService courseMaterialService) {
        this.courseMaterialService = courseMaterialService;
    }

    // 1. GET /api/materials
    @GetMapping
    public List<CourseMaterial> getAllMaterials() {
        return courseMaterialService.getAllMaterials();
    }

    // 2. GET /api/materials/course/{courseId}
    @GetMapping("/course/{courseId}")
    public List<CourseMaterial> getMaterialsByCourse(@PathVariable Long courseId) {
        return courseMaterialService.getMaterialsByCourse(courseId);
    }

    // 3. POST /api/materials
    @PostMapping
    public CourseMaterial createMaterial(@RequestBody CourseMaterialRequest request) {
        return courseMaterialService.createMaterial(request);
    }

    // 4. PUT /api/materials/{id}
    @PutMapping("/{id}")
    public CourseMaterial updateMaterial(@PathVariable Long id, @RequestBody CourseMaterialRequest request) {
        return courseMaterialService.updateMaterial(id, request);
    }

    // 5. DELETE /api/materials/{id}
    @DeleteMapping("/{id}")
    public void deleteMaterial(@PathVariable Long id) {
        courseMaterialService.deleteMaterial(id);
    }
}