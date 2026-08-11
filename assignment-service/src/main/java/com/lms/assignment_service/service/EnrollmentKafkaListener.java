package com.lms.assignment_service.service;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.lms.assignment_service.model.EnrollmentAccess;
import com.lms.assignment_service.repository.EnrollmentAccessRepository;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Service
public class EnrollmentKafkaListener {

    private final EnrollmentAccessRepository enrollmentAccessRepository;
    private final JsonMapper jsonMapper; // 引入 Jackson 的 JsonMapper 来解析 JSON

    // Constructor Injection (Spring Boot 会自动注入这两个 Bean)
    public EnrollmentKafkaListener(EnrollmentAccessRepository enrollmentAccessRepository, JsonMapper jsonMapper) {
        this.enrollmentAccessRepository = enrollmentAccessRepository;
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(topics = "course-enrolled", groupId = "assignment-service")
    public void handleCourseEnrolled(String message) {
        try {
            // 1. 解析 JSON String 变成 JsonNode
            JsonNode jsonNode = jsonMapper.readTree(message);
            
            // 2 & 3. 取得 studentId 和 courseId
            Long studentId = jsonNode.get("studentId").asLong();
            Long courseId = jsonNode.get("courseId").asLong();

            // 4. 检查是否已经存在记录
            Optional<EnrollmentAccess> existingAccess = enrollmentAccessRepository.findByStudentIdAndCourseId(studentId, courseId);
            
            EnrollmentAccess access;
            if (existingAccess.isPresent()) {
                // 如果以前选过后来退了，现在又重新选，就复用原本的 row
                access = existingAccess.get();
                access.setStatus("ACTIVE"); // 5. 确保状态是 ACTIVE
            } else {
                // 如果是第一次选课，就建立全新的 EnrollmentAccess
                access = new EnrollmentAccess(studentId, courseId, "ACTIVE");
            }

            // 6. save 进 Assignment Service 的数据库
            enrollmentAccessRepository.save(access);
            
            System.out.println("✅ [Assignment Service] Successfully processed course-enrolled -> Student: " + studentId + " | Course: " + courseId);

        } catch (JacksonException e) {
            System.err.println("❌ Error parsing course-enrolled message: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "course-dropped", groupId = "assignment-service")
    public void handleCourseDropped(String message) {
        try {
            // 1. 解析 JSON
            JsonNode jsonNode = jsonMapper.readTree(message);
            
            // 2 & 3. 取得 studentId 和 courseId
            Long studentId = jsonNode.get("studentId").asLong();
            Long courseId = jsonNode.get("courseId").asLong();

            // 4. 去 database 找出这笔记录
            Optional<EnrollmentAccess> existingAccess = enrollmentAccessRepository.findByStudentIdAndCourseId(studentId, courseId);
            
            if (existingAccess.isPresent()) {
                EnrollmentAccess access = existingAccess.get();
                // 5. 将状态改成 DROPPED
                access.setStatus("DROPPED");
                
                // 6. save 回 database
                enrollmentAccessRepository.save(access);
                
                System.out.println("⚠️ [Assignment Service] Successfully processed course-dropped -> Student: " + studentId + " | Course: " + courseId);
            } else {
                System.out.println("⚠️ [Assignment Service] Received course-dropped but no active access found for Student: " + studentId + " | Course: " + courseId);
            }

        } catch (JacksonException e) {
            System.err.println("❌ Error parsing course-dropped message: " + e.getMessage());
        }
    }
}