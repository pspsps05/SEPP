package com.example.demo.kafka; 
import com.example.demo.event.CourseDroppedEvent;
import com.example.demo.event.CourseEnrolledEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentEventProducer {

    private static final String ENROLLED_TOPIC = "course-enrolled";
    private static final String DROPPED_TOPIC = "course-dropped";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EnrollmentEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCourseEnrolled(CourseEnrolledEvent event) {
        kafkaTemplate.send(ENROLLED_TOPIC, event);
        System.out.println("✅ [Kafka Producer] Published to " + ENROLLED_TOPIC + 
                           " | Student: " + event.getStudentId() + " Course: " + event.getCourseId());
    }

    public void publishCourseDropped(CourseDroppedEvent event) {
        kafkaTemplate.send(DROPPED_TOPIC, event);
        System.out.println("⚠️ [Kafka Producer] Published to " + DROPPED_TOPIC + 
                           " | Student: " + event.getStudentId() + " Course: " + event.getCourseId());
    }
}