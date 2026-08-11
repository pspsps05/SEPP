package com.lms.course_service.service;

import com.lms.course_service.dto.CourseRequest;
import com.lms.course_service.model.Course;
import com.lms.course_service.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseEventPublisher eventPublisher;

    public CourseService(CourseRepository courseRepository, CourseEventPublisher eventPublisher) {
        this.courseRepository = courseRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(CourseRequest request) {
        Course course = new Course(
                request.getCourseCode(),
                request.getCourseName(),
                request.getDescription(),
                request.getInstructorName()
        );
        Course saved = courseRepository.save(course);

        // Publish event
        eventPublisher.publishCourseCreated(saved.getId(), saved.getCourseName());

        return saved;
    }

    public Course updateCourse(Long id, CourseRequest request) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        existing.setCourseCode(request.getCourseCode());
        existing.setCourseName(request.getCourseName());
        existing.setDescription(request.getDescription());
        existing.setInstructorName(request.getInstructorName());

        return courseRepository.save(existing);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
        eventPublisher.publishCourseDeleted(id);
    }
}