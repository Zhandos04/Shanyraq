package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.AnnouncementFilterRequest;
import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.mappers.AnnouncementMapper;
import by.project.turamyzba.services.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    private final ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/create")
    @Operation(summary = "Create a new announcement", description = "Creates a new announcement with provided parameters.")
    @ApiResponse(responseCode = "200", description = "Successfully created announcement")
    @ApiResponse(responseCode = "400", description = "Parameters are invalid")
    public ResponseEntity<?> createAnnouncement(@RequestBody @Valid AnnouncementRequest announcementRequest) {
        try {
            announcementService.createAnnouncement(announcementRequest);
            return ResponseEntity.ok("Announcement created successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating announcement: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findRoommates(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "41") int limit,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Announcement> roommatePage;

        if (search != null && !search.isEmpty()) {
            roommatePage = announcementService.searchRoommateListings(search, pageable);
        } else {
            roommatePage = announcementService.getAllRoommateListings(pageable);
        }

        List<AnnouncementResponse> announcementResponses = roommatePage.getContent().stream()
                .map(announcementService::toAnnouncementResponse)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("total", roommatePage.getTotalElements());
        response.put("page", page);
        response.put("limit", limit);
        response.put("data", announcementResponses);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        return ResponseEntity.ok(announcementService.getAnnouncementById(id));
    }

    @Operation(summary = "Получение объявлений пользователя", description = "Получение всех объявлений пользователя")
    @GetMapping("/my-active-announcements")
    public ResponseEntity<List<AnnouncementResponse>> getMyAnnouncements(){
        return ResponseEntity.ok(announcementService.getUserAnnouncements());
    }

    @Operation(summary = "Получение объявлений пользователя", description = "Получение всех объявлений пользователя")
    @GetMapping("/my-archive-announcements")
    public ResponseEntity<List<AnnouncementResponse>> getMyArchiveAnnouncements(){
        return ResponseEntity.ok(announcementService.getUserArchiveAnnouncements());
    }

    @PostMapping("/archive-announcement/{id}")
    @Operation(summary = "Архивация объявления", description = "Пользователь архивирует объявление")
    public ResponseEntity<?> archiveAnnouncement(@PathVariable Long id) throws BadRequestException {
        announcementService.archiveAnnouncement(id);
        return ResponseEntity.ok("Объявления успешно архивирован");
    }

    @Operation(summary = "Возвращает объявление в топ", description = "Пользователь возвращает объявление в топ")
    @PostMapping("/restore-announcement/{id}")
    public ResponseEntity<?> restoreAnnouncement(@PathVariable Long id) throws BadRequestException {
        announcementService.restoreAnnouncement(id);
        return ResponseEntity.ok("Объявления успешно возрващен в топ");
    }

    @Operation(summary = "Удаление объявление с архива", description = "Пользователь удаляет архивиронное объявление")
    @DeleteMapping("/delete-announcement/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) throws BadRequestException {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok("Объявления успешно удален");
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

    @GetMapping("/search")
    public ResponseEntity<?> getFilteredAnnouncements(@RequestBody AnnouncementFilterRequest request) {
        List<AnnouncementResponse> announcementResponses = announcementService.getFilteredAnnouncements(request).stream()
                .map(announcement -> modelMapper.map(announcement, AnnouncementResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(announcementResponses);
    }
}
