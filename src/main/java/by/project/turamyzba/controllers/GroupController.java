package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.GroupCreateDTO;
import by.project.turamyzba.dto.responses.LinkForSurveyDTO;
import by.project.turamyzba.services.GroupService;
import by.project.turamyzba.services.SurveyInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    @PostMapping("/create/{announcementId}")
    public ResponseEntity<LinkForSurveyDTO> createGroup(@PathVariable Long announcementId, @RequestBody GroupCreateDTO groupCreateDTO) {
        return ResponseEntity.ok(groupService.createGroup(announcementId, groupCreateDTO));
    }
}
