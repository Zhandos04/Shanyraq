package by.project.turamyzba.services;

import by.project.turamyzba.dto.responses.QuestionDTO;
import by.project.turamyzba.dto.responses.UserAnswerDTO;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.entities.anketa.UserAnswer;

import java.util.List;

public interface SurveyService {
    List<QuestionDTO> getAllQuestions();
    void saveUserAnswers(User user, List<UserAnswerDTO> userAnswers);
    boolean isSurveyCompleted(User user);
}
