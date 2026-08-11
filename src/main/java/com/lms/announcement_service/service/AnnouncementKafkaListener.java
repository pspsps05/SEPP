package com.lms.announcement_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.lms.announcement_service.model.Announcement;
import com.lms.announcement_service.repository.AnnouncementRepository;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Service
public class AnnouncementKafkaListener {

    private final AnnouncementRepository announcementRepository;
    private final JsonMapper jsonMapper;

    public AnnouncementKafkaListener(
            AnnouncementRepository announcementRepository,
            JsonMapper jsonMapper) {

        this.announcementRepository = announcementRepository;
        this.jsonMapper = jsonMapper;
    }

    @KafkaListener(
            topics = "lms-events",
            groupId = "announcement-service"
    )
    public void receiveEvent(String message) {

        System.out.println("Received event: " + message);

        Announcement announcement = new Announcement();

        switch (message) {

            case "CourseEnrolled":
                announcement.setTitle("Course Enrolment");
                announcement.setMessage("A student has enrolled in a course.");
                announcement.setEventType("CourseEnrolled");
                break;

            case "CourseDropped":
                announcement.setTitle("Course Withdrawal");
                announcement.setMessage("A student has dropped a course.");
                announcement.setEventType("CourseDropped");
                break;

            case "AssignmentCreated":
                announcement.setTitle("New Assignment");
                announcement.setMessage("A new assignment has been created.");
                announcement.setEventType("AssignmentCreated");
                break;

            case "AssignmentSubmitted":
                announcement.setTitle("Assignment Submitted");
                announcement.setMessage("An assignment has been submitted.");
                announcement.setEventType("AssignmentSubmitted");
                break;

            case "MaterialUploaded":
                announcement.setTitle("New Course Material");
                announcement.setMessage("New course material has been uploaded.");
                announcement.setEventType("MaterialUploaded");
                break;

            case "MaterialUpdated":
                announcement.setTitle("Course Material Updated");
                announcement.setMessage("Course material has been updated.");
                announcement.setEventType("MaterialUpdated");
                break;

            default:
                System.out.println("Unknown event: " + message);
                return;
        }

        announcementRepository.save(announcement);

        System.out.println("Announcement saved successfully.");
    }
    @KafkaListener(
        topics = "course-enrolled",
        groupId = "announcement-service"
    )
    public void receiveCourseEnrolled(String message) {

        try {
            JsonNode event = jsonMapper.readTree(message);

            Long studentId = event.get("studentId").asLong();
            Long courseId = event.get("courseId").asLong();
            String eventType = event.get("eventType").asText();

            System.out.println(
                    "Received course-enrolled event: Student "
                    + studentId + ", Course " + courseId
            );

            Announcement announcement = new Announcement();

            announcement.setTitle("Course Enrolment");

            announcement.setMessage(
                    "Student " + studentId
                    + " has enrolled in course "
                    + courseId + "."
            );

            announcement.setEventType(eventType);
            announcement.setCourseId(courseId);
            announcement.setRecipientId(studentId);

            announcementRepository.save(announcement);

            System.out.println(
                    "Course enrolment announcement saved."
            );

        } catch (Exception e) {
            System.err.println(
                    "Failed to process course-enrolled event: "
                    + e.getMessage()
            );
        }
    }
    @KafkaListener(
        topics = "course-dropped",
        groupId = "announcement-service"
    )
    public void receiveCourseDropped(String message) {

        try {
            JsonNode event = jsonMapper.readTree(message);

            Long studentId = event.get("studentId").asLong();
            Long courseId = event.get("courseId").asLong();
            String eventType = event.get("eventType").asText();

            System.out.println(
                    "Received course-dropped event: Student "
                    + studentId + ", Course " + courseId
            );

            Announcement announcement = new Announcement();

            announcement.setTitle("Course Withdrawal");

            announcement.setMessage(
                    "Student " + studentId
                    + " has withdrawn from course "
                    + courseId + "."
            );

            announcement.setEventType(eventType);
            announcement.setCourseId(courseId);
            announcement.setRecipientId(studentId);

            announcementRepository.save(announcement);

            System.out.println(
                    "Course withdrawal announcement saved."
            );

        } catch (Exception e) {
            System.err.println(
                    "Failed to process course-dropped event: "
                    + e.getMessage()
            );
        }
    }
}