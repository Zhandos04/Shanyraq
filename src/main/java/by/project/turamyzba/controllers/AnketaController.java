package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.AnketaDTO;
import by.project.turamyzba.services.AnketaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/anketa")
public class AnketaController {
    private final AnketaService anketaService;
    @PostMapping("/create")
    public ResponseEntity<?> createAnketa(@RequestBody List<AnketaDTO> anketaList) {
        anketaService.saveAnswers(anketaList);
        return ResponseEntity.ok("Anketa created successfully");
    }
    @GetMapping("/all")
    public ResponseEntity<List<AnketaDTO>> getAll() {
        return ResponseEntity.ok(anketaService.getAnswers());
    }
}
