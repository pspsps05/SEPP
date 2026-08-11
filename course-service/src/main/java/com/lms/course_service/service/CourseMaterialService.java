package com.lms.course_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.course_service.dto.CourseMaterialRequest;
import com.lms.course_service.model.CourseMaterial;
import com.lms.course_service.repository.CourseMaterialRepository;

@Service
public class CourseMaterialService {

    private final CourseMaterialRepository courseMaterialRepository;
    private final CourseEventPublisher eventPublisher;

    // Constructor Injection
    public CourseMaterialService(CourseMaterialRepository courseMaterialRepository, CourseEventPublisher eventPublisher) {
        this.courseMaterialRepository = courseMaterialRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<CourseMaterial> getAllMaterials() {
        return courseMaterialRepository.findAll();
    }

    public List<CourseMaterial> getMaterialsByCourse(Long courseId) {
        return courseMaterialRepository.findByCourseId(courseId);
    }

    // 3. create Material
    public CourseMaterial createMaterial(CourseMaterialRequest request) {
        CourseMaterial material = new CourseMaterial();
        
        material.setCourseId(request.getCourseId());
        material.setTitle(request.getTitle());
        material.setMaterialType(request.getMaterialType());
        material.setMaterialUrl(request.getMaterialUrl());
  
        LocalDateTime now = LocalDateTime.now();
        material.setUploadedAt(now);
        material.setUpdatedAt(now);

        CourseMaterial saved =
            courseMaterialRepository.save(material);

        eventPublisher.publishMaterialUploaded(
                saved.getId(),
                saved.getCourseId(),
                saved.getTitle()
        );
        return saved;
    }

    // 4. update Material
    public CourseMaterial updateMaterial(Long id, CourseMaterialRequest request) {
        CourseMaterial material = courseMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course material not found with ID: " + id));

        material.setCourseId(request.getCourseId());
        material.setTitle(request.getTitle());
        material.setMaterialType(request.getMaterialType());
        material.setMaterialUrl(request.getMaterialUrl());
        
        material.setUpdatedAt(LocalDateTime.now());

        CourseMaterial saved =
            courseMaterialRepository.save(material);

        eventPublisher.publishMaterialUpdated(
                saved.getId(),
                saved.getCourseId(),
                saved.getTitle()
        );

        return saved;
    }

    // 5. delete Material
    public void deleteMaterial(Long id) {
        if (!courseMaterialRepository.existsById(id)) {
            throw new RuntimeException("Course material not found with ID: " + id);
        }
        courseMaterialRepository.deleteById(id);
    }
}