package by.project.turamyzba.services.impl;

import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.ResidentData;
import by.project.turamyzba.entities.anketa.SurveyInvitation;
import by.project.turamyzba.exceptions.AnnouncementNotFoundException;
import by.project.turamyzba.exceptions.SurveyInvitationNotFoundException;
import by.project.turamyzba.repositories.AnnouncementRepository;
import by.project.turamyzba.repositories.anketa.SurveyInvitationForGroupRepository;
import by.project.turamyzba.repositories.anketa.SurveyInvitationRepository;
import by.project.turamyzba.services.SurveyInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SurveyInvitationServiceImpl implements SurveyInvitationService {
    private final SurveyInvitationRepository surveyInvitationRepository;
    private final AnnouncementRepository announcementRepository;
    private final SurveyInvitationForGroupRepository surveyInvitationForGroupRepository;
    @Override
    @Transactional
    public String createInvitationForAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new AnnouncementNotFoundException("Announcement not found!"));
        SurveyInvitation invitation = new SurveyInvitation();
        invitation.setAnnouncement(announcement);
        invitation.setToken(generateUniqueToken());
        invitation.setCreatedAt(LocalDateTime.now());
        surveyInvitationRepository.save(invitation);
        return invitation.getToken();
    }

    @Override
    public List<String> getNamesFromToken(String token) {
        SurveyInvitation surveyInvitation = surveyInvitationRepository.findByToken(token)
                .orElseThrow(() -> new SurveyInvitationNotFoundException("Survey invitation not found!"));

        Announcement announcement = surveyInvitation.getAnnouncement();
        List<String> names = new ArrayList<>();
        for (ResidentData residentData : announcement.getResidentData()) {
            names.add(residentData.getName());
        }
        return names;
    }

    private String generateUniqueToken() {
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (surveyInvitationForGroupRepository.existsByToken(token) && surveyInvitationRepository.existsByToken(token));
        return token;
    }
}
