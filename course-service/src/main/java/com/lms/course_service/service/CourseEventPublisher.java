package com.lms.course_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import tools.jackson.databind.json.JsonMapper;

@Service
public class CourseEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;

    // We'll send JSON to a specific topic (for future consumers) AND plain text to lms-events
    private static final String COURSE_EVENTS_TOPIC = "course-events";
    private static final String LMS_EVENTS_TOPIC = "lms-events";
    private static final String MATERIAL_EVENTS_TOPIC = "material-events";

    public CourseEventPublisher(
        KafkaTemplate<String, String> kafkaTemplate,
        JsonMapper jsonMapper) {

        this.kafkaTemplate = kafkaTemplate;
        this.jsonMapper = jsonMapper;
    }
    public void publishCourseCreated(Long courseId, String courseName) {
        // 1. Send JSON to course-events (for any microservice that wants detailed info)
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "CourseCreated");
        event.put("courseId", courseId);
        event.put("courseName", courseName);
        String json = jsonMapper.writeValueAsString(event);
        kafkaTemplate.send(COURSE_EVENTS_TOPIC, json);
        System.out.println("📤 [course-service] JSON event sent to " + COURSE_EVENTS_TOPIC);

        // 2. Send plain text to lms-events (for announcement-service)
        kafkaTemplate.send(LMS_EVENTS_TOPIC, "CourseCreated");
        System.out.println("📤 [course-service] Plain text sent to " + LMS_EVENTS_TOPIC);
    }

    public void publishCourseDeleted(Long courseId) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "CourseDeleted");
        event.put("courseId", courseId);
        String json = jsonMapper.writeValueAsString(event);
        kafkaTemplate.send(COURSE_EVENTS_TOPIC, json);

        // Also send to announcement-service (if you want)
        // kafkaTemplate.send(LMS_EVENTS_TOPIC, "CourseDeleted");

    }

    public void publishMaterialUploaded(
        Long materialId,
        Long courseId,
        String title) {

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "MaterialUploaded");
        event.put("materialId", materialId);
        event.put("courseId", courseId);
        event.put("title", title);

        String json = jsonMapper.writeValueAsString(event);

        // Detailed JSON event
        kafkaTemplate.send(MATERIAL_EVENTS_TOPIC, json);

        System.out.println(
                "[course-service] MaterialUploaded JSON sent to "
                + MATERIAL_EVENTS_TOPIC
        );

        // Plain text for Announcement Service
        kafkaTemplate.send(LMS_EVENTS_TOPIC, "MaterialUploaded");

        System.out.println(
                "[course-service] MaterialUploaded sent to "
                + LMS_EVENTS_TOPIC
        );
    }


    public void publishMaterialUpdated(
            Long materialId,
            Long courseId,
            String title) {

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "MaterialUpdated");
        event.put("materialId", materialId);
        event.put("courseId", courseId);
        event.put("title", title);

        String json = jsonMapper.writeValueAsString(event);

        // Detailed JSON event
        kafkaTemplate.send(MATERIAL_EVENTS_TOPIC, json);

        System.out.println(
                "[course-service] MaterialUpdated JSON sent to "
                + MATERIAL_EVENTS_TOPIC
        );

        // Plain text for Announcement Service
        kafkaTemplate.send(LMS_EVENTS_TOPIC, "MaterialUpdated");

        System.out.println(
                "[course-service] MaterialUpdated sent to "
                + LMS_EVENTS_TOPIC
        );
    }
}