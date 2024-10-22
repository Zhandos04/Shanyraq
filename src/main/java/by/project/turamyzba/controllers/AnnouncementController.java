package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.models.Announcement;
import by.project.turamyzba.services.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/findroommate")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @PostMapping("/create")
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody AnnouncementRequest announcementRequest) {
        Announcement announcement = announcementService.createAnnouncement(announcementRequest);
        return ResponseEntity.ok(announcement);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable Long id, @RequestBody AnnouncementRequest announcementRequest) {
        Announcement announcement = announcementService.updateAnnouncement(id, announcementRequest);
        return ResponseEntity.ok(announcement);
    }
}
