package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.SavedFilterDTO;
import by.project.turamyzba.dto.responses.SavedFilterResponseDTO;
import by.project.turamyzba.services.SavedFilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filters")
@Tag(name="SavedFilter", description="Взаймодействие с сохраненными фильтрами")
@RequiredArgsConstructor
public class SavedFilterController {

    private final SavedFilterService savedFilterService;

    @PostMapping("/save")
    @Operation(summary = "Сохранить фильтр")
    public ResponseEntity<HttpStatus> saveFilter(@RequestBody SavedFilterDTO filterDTO) {
        savedFilterService.saveFilter(filterDTO);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить фильтр")
    public ResponseEntity<List<SavedFilterResponseDTO>> deleteSavedFilter(@PathVariable Long id) {
        return ResponseEntity.ok(savedFilterService.deleteFilter(id));
    }
}
