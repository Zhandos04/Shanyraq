package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.SavedFilterDTO;
import by.project.turamyzba.services.SavedFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/filters")
@RequiredArgsConstructor
public class SavedFilterController {

    private final SavedFilterService savedFilterService;

    @PostMapping("/save")
    public ResponseEntity<HttpStatus> saveFilter(@RequestBody SavedFilterDTO filterDTO) {
        savedFilterService.saveFilter(filterDTO);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
