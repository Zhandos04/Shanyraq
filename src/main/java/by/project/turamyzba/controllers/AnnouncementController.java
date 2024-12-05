package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.AnnouncementFilterRequest;
import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.services.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/announcement")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @PostMapping("/create")
    @Operation(
            summary = "Объявление создать ету",
            description = "Осы announcementRequest бойынша объявление создать ету.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Announcement created successfully")
            }
    )
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
    @Operation(summary = "Барлык объявлениелерди алу.", description = "По дефолту 41 объявление береди. Показать еще баскан кезде" +
            "page ди инкремент жасап обратно осы эндпоинтка жибересиндер. sort ка дал дизайнда тургандай жибересиндер например Самые подходящие деп ешкандай ошибкасыз")
    public ResponseEntity<List<AnnouncementResponse>> findRoommates(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "41") int limit,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer roommatesCount,
            @RequestParam(required = false, defaultValue = "Самые подходящие") String sort) {
        Sort sortBy = getSort(sort);

        Pageable pageable = PageRequest.of(page - 1, limit, sortBy);
        Page<Announcement> roommatePage;

        if (region != null || minPrice != null || maxPrice != null || gender != null || roommatesCount != null) {
            roommatePage = announcementService.searchRoommateListings(region, minPrice, maxPrice, gender, roommatesCount, pageable);
        } else {
            roommatePage = announcementService.getAllRoommateListings(pageable);
        }

        List<AnnouncementResponse> announcementResponses = roommatePage.getContent().stream()
                .map(announcementService::toAnnouncementResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(announcementResponses);
    }
    private Sort getSort(String sort) {
        return switch (sort) {
            case "По возрастанию цены" -> Sort.by(Sort.Order.asc("cost")); // по возрастанию цены
            case "По убыванию цены" -> Sort.by(Sort.Order.desc("cost")); // по убыванию цены
            case "По новизне" -> Sort.by(Sort.Order.desc("arriveDate")); // по дате (новизне), по убыванию
            case "Самые подходящие" -> Sort.by(Sort.Order.desc("arriveDate"));
            default -> Sort.by(Sort.Order.desc("arriveDate")); // по умолчанию сортировка по новизне (убывание)
        };
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "Объявление детально алу.", description = "Барлык объявление алган кезде ар объявлениенин айдишкасыда барады" +
            " вот сол айдишканы осы эндпоинт жибересиндер детально алу ушин.")
    public ResponseEntity<AnnouncementResponse> detail(@PathVariable Long id) {
        return ResponseEntity.ok(announcementService.getAnnouncementById(id));
    }

    @Operation(summary = "Определенный пользовательдин активный объявлениелерин алу.", description = "Ешкандай зат жибермейсиндер кроме токена токен аркылы кай " +
            "пользователь жиберип тур соны аныктап сонын объявлениелерин жиберемин.")
    @GetMapping("/my-active-announcements")
    public ResponseEntity<List<AnnouncementResponse>> getMyAnnouncements(){
        return ResponseEntity.ok(announcementService.getUserAnnouncements());
    }

    @Operation(summary = "Определенный пользовательдин архивный объявлениелерин алу.", description = "Ешкандай зат жибермейсиндер кроме токена токен аркылы кай " +
            "пользователь жиберип тур соны аныктап сонын объявлениелерин жиберемин.")
    @GetMapping("/my-archive-announcements")
    public ResponseEntity<List<AnnouncementResponse>> getMyArchiveAnnouncements(){
        return ResponseEntity.ok(announcementService.getUserArchiveAnnouncements());
    }

    @PostMapping("/archive-announcement/{id}")
    @Operation(summary = "Архивация объявления", description = "Пользователь архивирует объявление. Объявлениенин айдиын жибересиндер.")
    public ResponseEntity<?> archiveAnnouncement(@PathVariable Long id) throws BadRequestException {
        announcementService.archiveAnnouncement(id);
        return ResponseEntity.ok("Объявления успешно архивирован");
    }

    @Operation(summary = "Возвращает объявление в топ", description = "Пользователь возвращает объявление в топ. Объявлениенин айдиын жибересиндер.")
    @PostMapping("/restore-announcement/{id}")
    public ResponseEntity<?> restoreAnnouncement(@PathVariable Long id) throws BadRequestException {
        announcementService.restoreAnnouncement(id);
        return ResponseEntity.ok("Объявления успешно возрващен в топ");
    }

    @Operation(summary = "Удаление объявление с архива", description = "Пользователь удаляет архивиронное объявление. Объявлениенин айдиын жибересиндер.")
    @DeleteMapping("/delete-announcement/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) throws BadRequestException {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok("Объявления успешно удален");
    }

    @GetMapping("/great-deals")
    @Operation(summary = "Выгодные предложения", description = "Пока что чисто по возрастнию цены объявлениелер кайтарады 10 штук.")
    public ResponseEntity<List<AnnouncementResponse>> greatDeals() {
        Sort sortBy = getSort("По возрастанию цены");
        Pageable pageable = PageRequest.of(0, 10, sortBy);
        Page<Announcement> roommatePage = announcementService.getAllRoommateListings(pageable);
        List<AnnouncementResponse> announcementResponses = roommatePage.getContent().stream()
                .map(announcementService::toAnnouncementResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(announcementResponses);
    }

    @GetMapping("/search")
    @Operation(summary = "Фильтр")
    public ResponseEntity<List<AnnouncementResponse>> getFilteredAnnouncements(@RequestBody AnnouncementFilterRequest request) {
        List<AnnouncementResponse> announcementResponses = announcementService.getFilteredAnnouncements(request).stream()
                .map(announcementService::toAnnouncementResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(announcementResponses);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update an announcement", description = "Updates an announcement with provided parameters.")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(@PathVariable Long id, @RequestBody @Valid AnnouncementRequest announcementRequest) {
        AnnouncementResponse announcementResponse = announcementService.updateAnnouncement(id, announcementRequest);
        return ResponseEntity.ok(announcementResponse);
    }
}
