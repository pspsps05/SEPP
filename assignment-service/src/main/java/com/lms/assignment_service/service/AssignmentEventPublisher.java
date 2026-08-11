package com.lms.assignment_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AssignmentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String LMS_EVENTS_TOPIC = "lms-events";

    public AssignmentEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAssignmentCreated(Long assignmentId, String title) {
        // Plain text to lms-events (for announcement-service)
        kafkaTemplate.send(LMS_EVENTS_TOPIC, "AssignmentCreated");
        System.out.println("📤 [assignment-service] Sent 'AssignmentCreated' to " + LMS_EVENTS_TOPIC);
    }

    public void publishAssignmentSubmitted(Long assignmentId, Long studentId) {
        kafkaTemplate.send(LMS_EVENTS_TOPIC, "AssignmentSubmitted");
        System.out.println("📤 [assignment-service] Sent 'AssignmentSubmitted' to " + LMS_EVENTS_TOPIC);
    }
}