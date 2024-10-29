package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.services.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/findroommate")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @PostMapping("/create")
    @Operation(summary = "Create a new announcement", description = "Creates a new announcement with provided parameters.")
    @ApiResponse(responseCode = "200", description = "Successfully created announcement")
    @ApiResponse(responseCode = "400", description = "Parameters are invalid")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(@RequestBody @Valid AnnouncementRequest announcementRequest) {
        AnnouncementResponse announcementResponse = announcementService.createAnnouncement(announcementRequest);
        return ResponseEntity.ok(announcementResponse);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update an announcement", description = "Updates an announcement with provided parameters.")
    @ApiResponse(responseCode = "200", description = "Successfully updated announcement")
    @ApiResponse(responseCode = "400", description = "Parameters are invalid")
    @ApiResponse(responseCode = "403", description = "User not authorized to update this announcement")
    @ApiResponse(responseCode = "404", description = "Announcement not found")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(@PathVariable Long id, @RequestBody @Valid AnnouncementRequest announcementRequest) {
        AnnouncementResponse announcementResponse = announcementService.updateAnnouncement(id, announcementRequest);
        return ResponseEntity.ok(announcementResponse);
    }
}
