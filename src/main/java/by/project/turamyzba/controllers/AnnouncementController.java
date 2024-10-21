package by.project.turamyzba.controllers;

import by.project.turamyzba.models.AnnouncementUser;
import by.project.turamyzba.services.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController()
@RequestMapping("/api")
public class AnnouncementController {
    @Autowired
    AnnouncementService announcementService;
    @GetMapping("/my-announcement")
    public ResponseEntity<List<AnnouncementUser>> getMyAnnouncement(@RequestParam Long id){
        List<AnnouncementUser> announcements = announcementService.getUserAnnouncements(id);
        return ResponseEntity.ok(announcements);
    }
//    @PostMapping("/archive-announcement/{id}")
//    public String archiveAnnouncement(@PathVariable Long id){
//        return announcementService.archiveAnnouncement(id).toString();
//    }
//    @PostMapping("/restore-announcement/{id}")
//    public String restoreAnnouncement(@PathVariable Long id){
//        return announcementService.restoreAnnouncements(id).toString();
//    }
//    @DeleteMapping("/delete-announcement/{id}")
//    public void deleteAnnouncement(@PathVariable Long id){
//        announcementService.deletedAnnouncement(id);
//    }
}
