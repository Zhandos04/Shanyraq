package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.GroupCreateDTO;
import by.project.turamyzba.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    @PostMapping("/create/{announcementId}")
    public ResponseEntity<?> createGroup(@PathVariable Long announcementId, @RequestBody GroupCreateDTO groupCreateDTO) {
        groupService.createGroup(announcementId, groupCreateDTO);
    }
}
