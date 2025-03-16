package by.project.turamyzba.services.impl;

import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.Group;
import by.project.turamyzba.entities.GroupMember;
import by.project.turamyzba.entities.ResidentData;
import by.project.turamyzba.entities.anketa.SurveyInvitation;
import by.project.turamyzba.entities.anketa.SurveyInvitationForGroup;
import by.project.turamyzba.exceptions.AnnouncementNotFoundException;
import by.project.turamyzba.exceptions.SurveyInvitationNotFoundException;
import by.project.turamyzba.repositories.AnnouncementRepository;
import by.project.turamyzba.repositories.GroupMemberRepository;
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
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SurveyInvitationServiceImpl implements SurveyInvitationService {
    private final SurveyInvitationRepository surveyInvitationRepository;
    private final AnnouncementRepository announcementRepository;
    private final SurveyInvitationForGroupRepository surveyInvitationForGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
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
        Optional<SurveyInvitation> optionalSurveyInvitation = surveyInvitationRepository.findByToken(token);
        Optional<SurveyInvitationForGroup> optionalSurveyInvitationForGroup = surveyInvitationForGroupRepository.findByToken(token);
        List<String> names = new ArrayList<>();
        if (optionalSurveyInvitation.isEmpty() && optionalSurveyInvitationForGroup.isEmpty()) {
            throw new SurveyInvitationNotFoundException("Survey invitation not found!");
        } else if (optionalSurveyInvitation.isPresent()) {
            Announcement announcement = optionalSurveyInvitation.get().getAnnouncement();
            for (ResidentData residentData : announcement.getResidentData()) {
                names.add(residentData.getName());
            }
        } else {
            Group group = optionalSurveyInvitationForGroup.get().getGroup();
            List<GroupMember> groupMembers = groupMemberRepository.findAllByGroup(group);
            for (GroupMember groupMember : groupMembers) {
                if (groupMember.getUser() == null) {
                    names.add(groupMember.getName());
                }
            }
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
