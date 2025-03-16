package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.SubmitApplicationDTO;
import by.project.turamyzba.dto.responses.LinkForSurveyDTO;
import by.project.turamyzba.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping("/create/{groupId}")
    public ResponseEntity<LinkForSurveyDTO> createApplication(@PathVariable Long groupId, @RequestBody SubmitApplicationDTO submitApplicationDTO) {
        return ResponseEntity.ok(applicationService.createApplication(groupId, submitApplicationDTO));
    }
}
