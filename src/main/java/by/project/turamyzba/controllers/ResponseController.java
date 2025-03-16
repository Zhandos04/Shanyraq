package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.responses.application.ApplicationResponseDTO;
import by.project.turamyzba.services.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/response")
public class ResponseController {
    private final ResponseService responseService;

    @GetMapping("/all/{announcementId}")
    public ResponseEntity<ApplicationResponseDTO> getAllResponsesForMyAnnouncement(@PathVariable Long announcementId) {
        return ResponseEntity.ok(responseService.getAllResponsesForMyAnnouncement(announcementId));
    }
}
