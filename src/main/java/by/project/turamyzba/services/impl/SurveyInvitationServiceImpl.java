package by.project.turamyzba.services.impl;

import by.project.turamyzba.entities.anketa.SurveyInvitation;
import by.project.turamyzba.repositories.anketa.SurveyInvitationRepository;
import by.project.turamyzba.services.SurveyInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

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
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setCreatedAt(LocalDateTime.now());
        surveyInvitationRepository.save(invitation);
        return invitation;
    }
}
