package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.responses.QuestionDTO;
import by.project.turamyzba.dto.responses.UserAnswerDTO;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.entities.anketa.Question;
import by.project.turamyzba.entities.anketa.UserAnswer;
import by.project.turamyzba.services.SurveyService;
import by.project.turamyzba.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;
    private final UserService userService;
    @GetMapping("/questions")
    public List<QuestionDTO> getAllQuestions() {
        return surveyService.getAllQuestions();
    }
    @PostMapping("/submit")
    public ResponseEntity<String> submitAnswers(@RequestBody List<UserAnswerDTO> userAnswers) {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        surveyService.saveUserAnswers(user, userAnswers);
        return ResponseEntity.ok("Answers submitted successfully!");
    }

}
