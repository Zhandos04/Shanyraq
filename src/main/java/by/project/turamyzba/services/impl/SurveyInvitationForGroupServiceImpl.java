package by.project.turamyzba.services.impl;

import by.project.turamyzba.entities.Group;
import by.project.turamyzba.entities.anketa.SurveyInvitationForGroup;
import by.project.turamyzba.exceptions.GroupNotFoundException;
import by.project.turamyzba.repositories.GroupRepository;
import by.project.turamyzba.repositories.anketa.SurveyInvitationForGroupRepository;
import by.project.turamyzba.services.SurveyInvitationForGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyInvitationForGroupServiceImpl implements SurveyInvitationForGroupService {
    private final GroupRepository groupRepository;
    private final SurveyInvitationForGroupRepository surveyInvitationForGroupRepository;
    @Override
    @Transactional
    public String createInvitationForGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found!"));
        SurveyInvitationForGroup invitation = new SurveyInvitationForGroup();
        invitation.setGroup(group);
        invitation.setToken(generateToken(id));
        invitation.setCreatedAt(LocalDateTime.now());
        surveyInvitationForGroupRepository.save(invitation);
        return invitation.getToken();
    }

    private String generateToken(Long id) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = id + "-" + System.nanoTime(); // Добавляем временную метку для уникальности
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash); // Кодируем в base64 для удобства
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка генерации токена", e);
        }
    }
}
