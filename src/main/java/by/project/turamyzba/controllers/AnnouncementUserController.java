package by.project.turamyzba.controllers;

import by.project.turamyzba.entities.AnnouncementUser;
import by.project.turamyzba.services.AnnouncementUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController()
@RequestMapping("/announcement")
@Tag(name="AnnouncementUser", description="Взаимодействие с объявлениями пользователя")
@RequiredArgsConstructor
public class AnnouncementUserController {
    private final AnnouncementUserService announcementUserService;

    @Operation(summary = "Получение объявлений пользователя", description = "Получение всех объявлений пользователя")
    @GetMapping("/my-announcement")
    public ResponseEntity<List<AnnouncementUser>> getMyAnnouncement(@RequestParam Long id){
        List<AnnouncementUser> announcements = announcementUserService.getUserAnnouncements(id);
        return ResponseEntity.ok(announcements);
    }

    @Operation(summary = "Архивация объявления", description = "Пользователь архивирует объявление")
    @PostMapping("/archive-announcement/{id}")
    public String archiveAnnouncement(@PathVariable Long id){
        return announcementUserService.archiveAnnouncement(id).toString();
    }

    @Operation(summary = "Возвращает объявление в топ", description = "Пользователь возвращает объявление в топ")
    @PutMapping("/restore-announcement/{id}")
    public String restoreAnnouncement(@PathVariable Long id){
        return announcementUserService.restoreAnnouncement(id).toString();
    }

    @Operation(summary = "Удаление объявление с архива", description = "Пользователь удаляет архивиронное объявление")
    @DeleteMapping("/delete-announcement/{id}")
    public void deleteAnnouncement(@PathVariable Long id){
        announcementUserService.deleteAnnouncement(id);
    }
}
