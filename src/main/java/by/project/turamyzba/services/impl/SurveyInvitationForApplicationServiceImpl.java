package by.project.turamyzba.services.impl;

import by.project.turamyzba.entities.anketa.SurveyInvitationForApplication;
import by.project.turamyzba.repositories.anketa.SurveyInvitationForApplicationRepository;
import by.project.turamyzba.repositories.anketa.SurveyInvitationForGroupRepository;
import by.project.turamyzba.repositories.anketa.SurveyInvitationRepository;
import by.project.turamyzba.services.SurveyInvitationForApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyInvitationForApplicationServiceImpl implements SurveyInvitationForApplicationService {
    private final SurveyInvitationRepository surveyInvitationRepository;
    private final SurveyInvitationForGroupRepository surveyInvitationForGroupRepository;
    private final SurveyInvitationForApplicationRepository surveyInvitationForApplicationRepository;
    @Override
    @Transactional
    public String createInvitationForApplication(String applicationBatchId) {
        SurveyInvitationForApplication surveyInvitationForApplication = new SurveyInvitationForApplication();
        surveyInvitationForApplication.setApplicationBatchId(applicationBatchId);
        surveyInvitationForApplication.setCreatedAt(LocalDateTime.now());
        surveyInvitationForApplication.setToken(generateUniqueToken());
        surveyInvitationForApplicationRepository.save(surveyInvitationForApplication);
        return surveyInvitationForApplication.getToken();
    }

    @Override
    public List<String> getNamesFromToken(String token) {
        return null;
    }

    private String generateUniqueToken() {
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (surveyInvitationForApplicationRepository.existsByToken(token) && surveyInvitationForGroupRepository.existsByToken(token) && surveyInvitationRepository.existsByToken(token));
        return token;
    }
}
