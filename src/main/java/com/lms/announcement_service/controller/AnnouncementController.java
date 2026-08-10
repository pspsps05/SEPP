package com.lms.announcement_service.controller;

import com.lms.announcement_service.model.Announcement;
import com.lms.announcement_service.service.AnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public List<Announcement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable Long id) {
        return announcementService.getAnnouncementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(
            @RequestBody Announcement announcement) {

        return ResponseEntity.ok(
                announcementService.createAnnouncement(announcement)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody Announcement announcement) {

        return ResponseEntity.ok(
                announcementService.updateAnnouncement(id, announcement)
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Announcement> markAsRead(@PathVariable Long id) {

        return ResponseEntity.ok(
                announcementService.markAsRead(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {

        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }
}