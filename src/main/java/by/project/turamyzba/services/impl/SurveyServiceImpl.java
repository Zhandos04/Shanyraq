package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.responses.*;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.entities.anketa.Option;
import by.project.turamyzba.entities.anketa.Question;
import by.project.turamyzba.entities.anketa.UserAnswer;
import by.project.turamyzba.repositories.UserRepository;
import by.project.turamyzba.repositories.anketa.QuestionRepository;
import by.project.turamyzba.repositories.anketa.UserAnswerRepository;
import by.project.turamyzba.services.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyServiceImpl implements SurveyService {
    private final QuestionRepository questionRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserRepository userRepository;

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
        user.setIsSurveyCompleted(true);
        userRepository.save(user);
    }

    @Override
    public SurveyResponseDTO viewSurvey(Long id) {
        User user = userRepository.getUserById(id);
        SurveyResponseDTO surveyResponseDTO = new SurveyResponseDTO();
        surveyResponseDTO.setFullName(user.getFirstName() + " " + user.getLastName());

        List<SurveyAnswerDTO> answers = userAnswerRepository.findAllByUser(user)
                .stream()
                .map(userAnswer -> {
                    SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
                    surveyAnswerDTO.setQuestion(userAnswer.getQuestion().getText());
                    surveyAnswerDTO.setAnswer(userAnswer.getOption().getText());
                    return surveyAnswerDTO;
                })
                .collect(Collectors.toList());

        surveyResponseDTO.setAnswers(answers);
        return surveyResponseDTO;
    }


    // Вспомогательный метод для преобразования Question в DTO
    private QuestionDTO convertToDTO(Question question) {
        List<OptionDTO> options = question.getOptions().stream()
                .map(option -> new OptionDTO(option.getId(), option.getText()))
                .toList();
        return new QuestionDTO(question.getId(), question.getText(), options);
    }
}
