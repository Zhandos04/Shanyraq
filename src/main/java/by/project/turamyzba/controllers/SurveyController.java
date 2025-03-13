package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.responses.QuestionDTO;
import by.project.turamyzba.dto.responses.SurveyResponseDTO;
import by.project.turamyzba.dto.responses.UserAnswerDTO;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.services.SurveyService;
import by.project.turamyzba.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/survey")
@Tag(name="Survey", description="Взаймодействие с анкетой")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;
    private final UserService userService;
    @GetMapping("/questions")
    @Operation(summary = "Анкетанын барлык сурактарын алу")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        return ResponseEntity.ok(surveyService.getAllQuestions());
    }
    @PostMapping("/submit")
    @Operation(summary = "Барлык анкетаны толтырып жиберу", description = "Суракпен тандаган жауапты жибересиндер")
    public ResponseEntity<Boolean> submitAnswers(@RequestBody List<UserAnswerDTO> userAnswers) {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        surveyService.saveUserAnswers(user, userAnswers);
        Boolean isSurveyCompleted = true;
        return ResponseEntity.ok(isSurveyCompleted);
    }
    @GetMapping("/view/{id}")
    @Operation(summary = "Пользовательдин анкетасын кору", description = "Посмотреть анкету басканда юзердин айдиын жиберип path аркылы аласын анкетаны")
    public ResponseEntity<SurveyResponseDTO> viewSurvey(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.viewSurvey(id));
    }
}
