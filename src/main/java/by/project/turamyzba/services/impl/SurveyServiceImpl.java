package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.responses.OptionDTO;
import by.project.turamyzba.dto.responses.QuestionDTO;
import by.project.turamyzba.dto.responses.UserAnswerDTO;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.entities.anketa.Option;
import by.project.turamyzba.entities.anketa.Question;
import by.project.turamyzba.entities.anketa.UserAnswer;
import by.project.turamyzba.entities.anketa.UserStatus;
import by.project.turamyzba.repositories.anketa.QuestionRepository;
import by.project.turamyzba.repositories.anketa.UserAnswerRepository;
import by.project.turamyzba.repositories.anketa.UserStatusRepository;
import by.project.turamyzba.services.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyServiceImpl implements SurveyService {
    private final QuestionRepository questionRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserStatusRepository userStatusRepository;

    // Получение всех вопросов с преобразованием в DTO
    @Override
    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream().map(this::convertToDTO).toList();
    }

    // Сохранение ответов пользователя
    @Override
    @Transactional
    public void saveUserAnswers(User user, List<UserAnswerDTO> userAnswers) {
        // Удалить предыдущие ответы пользователя
        userAnswerRepository.deleteAllByUser(user);

        // Преобразование DTO в UserAnswer и установка пользователя
        List<UserAnswer> answers = userAnswers.stream().map(dto -> {
            UserAnswer answer = new UserAnswer();

            Question question = new Question();
            question.setId(dto.getQuestionId());
            answer.setQuestion(question);

            Option option = new Option();
            option.setId(dto.getOptionId());
            answer.setOption(option);

            answer.setUser(user);
            return answer;
        }).toList();

        // Сохранить ответы
        userAnswerRepository.saveAll(answers);

        // Обновить статус пользователя
        UserStatus status = userStatusRepository.findByUser(user)
                .orElse(new UserStatus());
        status.setUser(user);
        status.setSurveyCompleted(true);
        userStatusRepository.save(status);
    }

    // Проверка, прошел ли пользователь анкету
    @Override
    public boolean isSurveyCompleted(User user) {
        return userStatusRepository.findByUser(user)
                .map(UserStatus::isSurveyCompleted)
                .orElse(false);
    }

    // Вспомогательный метод для преобразования Question в DTO
    private QuestionDTO convertToDTO(Question question) {
        List<OptionDTO> options = question.getOptions().stream()
                .map(option -> new OptionDTO(option.getText()))
                .toList();
        return new QuestionDTO(question.getText(), options);
    }
}
