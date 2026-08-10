package com.lms.announcement_service.service;

import com.lms.announcement_service.model.Announcement;
import com.lms.announcement_service.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepository.findById(id);
    }

    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    public Announcement updateAnnouncement(Long id, Announcement announcement) {
        return announcementRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(announcement.getTitle());
                    existing.setMessage(announcement.getMessage());
                    existing.setEventType(announcement.getEventType());
                    existing.setCourseId(announcement.getCourseId());
                    existing.setRecipientId(announcement.getRecipientId());
                    existing.setRead(announcement.isRead());

                    return announcementRepository.save(existing);
                })
                .orElseThrow(() ->
                        new RuntimeException("Announcement not found with id: " + id));
    }

    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }

    public Announcement markAsRead(Long id) {
        return announcementRepository.findById(id)
                .map(announcement -> {
                    announcement.setRead(true);
                    return announcementRepository.save(announcement);
                })
                .orElseThrow(() ->
                        new RuntimeException("Announcement not found with id: " + id));
    }
}