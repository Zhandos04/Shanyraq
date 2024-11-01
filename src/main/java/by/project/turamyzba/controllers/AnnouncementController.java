package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.AnnouncementFilterRequest;
import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.AnnouncementUser;
import by.project.turamyzba.services.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/findroommate")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    private ModelMapper modelMapper = new ModelMapper();

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
        AnnouncementResponse announcementResponse = modelMapper.map(announcementService.getAnnouncementById(id), AnnouncementResponse.class);
        return ResponseEntity.ok(announcementResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getFilteredAnnouncements(@RequestBody AnnouncementFilterRequest request) {
        List<AnnouncementResponse> announcementResponses = announcementService.getFilteredAnnouncements(request).stream()
                .map(announcement -> modelMapper.map(announcement, AnnouncementResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(announcementResponses);
    }

    @Operation(summary = "Получение объявлений пользователя", description = "Получение всех объявлений пользователя")
    @GetMapping("/my-announcement")
    public ResponseEntity<List<AnnouncementResponse>> getMyAnnouncement(@RequestParam Long id){
        List<AnnouncementResponse> announcementResponses = announcementService.getUserAnnouncements(id).stream()
                .map(announcement -> modelMapper.map(announcement, AnnouncementResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(announcementResponses);
    }

    @Operation(summary = "Архивация объявления", description = "Пользователь архивирует объявление")
    @PostMapping("/archive-announcement/{id}")
    public ResponseEntity<?> archiveAnnouncement(@PathVariable Long id){
        return ResponseEntity.ok(announcementService.archiveAnnouncement(id));
    }

    @Operation(summary = "Возвращает объявление в топ", description = "Пользователь возвращает объявление в топ")
    @PostMapping("/restore-announcement/{id}")
    public ResponseEntity<?> restoreAnnouncement(@PathVariable Long id){
        return ResponseEntity.ok(announcementService.restoreAnnouncement(id));
    }

    @Operation(summary = "Удаление объявление с архива", description = "Пользователь удаляет архивиронное объявление")
    @DeleteMapping("/delete-announcement/{id}")
    public void deleteAnnouncement(@PathVariable Long id){
        announcementService.deleteAnnouncement(id);
    }
}
