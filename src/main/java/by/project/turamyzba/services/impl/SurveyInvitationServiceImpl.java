package by.project.turamyzba.services.impl;

import by.project.turamyzba.entities.anketa.SurveyInvitation;
import by.project.turamyzba.repositories.anketa.SurveyInvitationRepository;
import by.project.turamyzba.services.SurveyInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SurveyInvitationServiceImpl implements SurveyInvitationService {
    private final SurveyInvitationRepository surveyInvitationRepository;
    @Override
    @Transactional
    public SurveyInvitation createInvitation(Long announcementId) {
        SurveyInvitation invitation = new SurveyInvitation();
        invitation.setAnnouncementId(announcementId);
        invitation.setToken(generateToken(announcementId));
        invitation.setCreatedAt(LocalDateTime.now());
        surveyInvitationRepository.save(invitation);
        return invitation;
    }

    private String generateToken(Long announcementId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = announcementId + "-" + System.nanoTime(); // Добавляем временную метку для уникальности
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash); // Кодируем в base64 для удобства
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка генерации токена", e);
        }
    }
}
