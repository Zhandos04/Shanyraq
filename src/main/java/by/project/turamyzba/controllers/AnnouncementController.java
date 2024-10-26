package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.AnnouncementFilterRequest;
import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.services.AnnouncementService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/findroommate")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @PostMapping("/create")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(@RequestBody @Valid AnnouncementRequest announcementRequest) {
        AnnouncementResponse announcementResponse = announcementService.createAnnouncement(announcementRequest);
        return ResponseEntity.ok(announcementResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(@PathVariable Long id, @RequestBody @Valid AnnouncementRequest announcementRequest) {
        AnnouncementResponse announcementResponse = announcementService.updateAnnouncement(id, announcementRequest);
        return ResponseEntity.ok(announcementResponse);
    }

    @GetMapping("/findroommates")
    public ResponseEntity<?> findRoommates(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "40") int limit,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Announcement> roommatePage;

        if (search != null && !search.isEmpty()) {
            roommatePage = announcementService.searchRoommateListings(search, pageable);
        } else {
            roommatePage = announcementService.getAllRoommateListings(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("total", roommatePage.getTotalElements());
        response.put("page", page);
        response.put("limit", limit);
        response.put("data", roommatePage.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnnouncementById(@PathVariable Long id) {
        Announcement announcement = announcementService.getAnnouncementById(id);
        return ResponseEntity.ok(announcement);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getFilteredAnnouncements(@RequestBody AnnouncementFilterRequest request) {
        List<Announcement> announcements = announcementService.getFilteredAnnouncements(request);
        return ResponseEntity.ok(announcements);
    }
}
