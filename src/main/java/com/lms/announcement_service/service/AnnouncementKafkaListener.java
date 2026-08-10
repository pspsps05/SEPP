package com.lms.announcement_service.service;

import com.lms.announcement_service.model.Announcement;
import com.lms.announcement_service.repository.AnnouncementRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementKafkaListener {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementKafkaListener(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
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
}