package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.LoginDTO;
import by.project.turamyzba.dto.requests.SurveyFromLinkDTO;
import by.project.turamyzba.dto.responses.*;
import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.AnnouncementResident;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.entities.anketa.*;
import by.project.turamyzba.exceptions.SurveyInvitationNotFoundException;
import by.project.turamyzba.repositories.AnnouncementResidentRepository;
import by.project.turamyzba.repositories.UserRepository;
import by.project.turamyzba.repositories.anketa.QuestionRepository;
import by.project.turamyzba.repositories.anketa.SurveyInvitationRepository;
import by.project.turamyzba.repositories.anketa.UserAnswerRepository;
import by.project.turamyzba.services.EmailService;
import by.project.turamyzba.services.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyServiceImpl implements SurveyService {
    private final QuestionRepository questionRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final UserRepository userRepository;
    private final SurveyInvitationRepository surveyInvitationRepository;
    private final AnnouncementResidentRepository announcementResidentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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
                .toList();

        surveyResponseDTO.setAnswers(answers);
        return surveyResponseDTO;
    }

    @Override
    @Transactional
    public void saveSurveyAnswersFromLink(SurveyFromLinkDTO surveyFromLinkDTO) {
        SurveyInvitation surveyInvitation = surveyInvitationRepository.findByToken(surveyFromLinkDTO.getToken())
                .orElseThrow(() -> new SurveyInvitationNotFoundException("Survey invitation not found!"));

        Announcement announcement = surveyInvitation.getAnnouncement();

        User resident;
        Optional<User> optionalUser = userRepository.findByEmail(surveyFromLinkDTO.getEmail());
        if (optionalUser.isEmpty()) {
            resident = new User();
            resident.setFirstName(surveyFromLinkDTO.getFirstName());
            resident.setBirthDate(LocalDate.parse(surveyFromLinkDTO.getBirthDate()));
            resident.setPhoneNumber(surveyFromLinkDTO.getPhoneNumber());
            resident.setEmail(surveyFromLinkDTO.getEmail());
            resident.setGender(surveyFromLinkDTO.getGender());
            resident.setPassword(passwordEncoder.encode(surveyFromLinkDTO.getPassword()));
            resident.setIsSurveyCompleted(true);
            resident.setIsVerified(false);
            String code = generateCode();
            resident.setConfirmationCode(code);
            try {
                emailService.sendEmail(surveyFromLinkDTO.getEmail(), "Shanyraq Verify Email", "Your code is: " + code);
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while sending the verification email.");
            }
            userRepository.save(resident);
        } else {
            resident = optionalUser.get();
        }

        AnnouncementResident announcementResident = new AnnouncementResident();
        announcementResident.setAnnouncement(announcement);
        announcementResident.setUser(resident);
        announcementResidentRepository.save(announcementResident);

        List<UserAnswer> answers = surveyFromLinkDTO.getUserAnswers().stream().map(dto -> {
            UserAnswer answer = new UserAnswer();

            Question question = new Question();
            question.setId(dto.getQuestionId());
            answer.setQuestion(question);

            Option option = new Option();
            option.setId(dto.getOptionId());
            answer.setOption(option);

            answer.setUser(resident);
            return answer;
        }).toList();

        userAnswerRepository.saveAll(answers);
    }

    @Override
    public UserDataResponse checkAccountAndGetData(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User with this email not found!"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Неправильный пароль!");
        }
        UserDataResponse userDataResponse = new UserDataResponse();
        userDataResponse.setFirstName(user.getFirstName());
        userDataResponse.setPhoneNumber(user.getPhoneNumber());
        userDataResponse.setBirthDate(user.getBirthDate().toString());
        userDataResponse.setGender(user.getGender());

        List<SurveyAnswerDTO> answers = userAnswerRepository.findAllByUser(user)
                .stream()
                .map(userAnswer -> {
                    SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
                    surveyAnswerDTO.setQuestion(userAnswer.getQuestion().getText());
                    surveyAnswerDTO.setAnswer(userAnswer.getOption().getText());
                    return surveyAnswerDTO;
                })
                .toList();

        userDataResponse.setSurveyAnswers(answers);
        return userDataResponse;
    }

    private String generateCode() {
        return Integer.toString((int)(Math.random() * 900000) + 100000);
    }


    // Вспомогательный метод для преобразования Question в DTO
    private QuestionDTO convertToDTO(Question question) {
        List<OptionDTO> options = question.getOptions().stream()
                .map(option -> new OptionDTO(option.getId(), option.getText()))
                .toList();
        return new QuestionDTO(question.getId(), question.getText(), options);
    }
}
