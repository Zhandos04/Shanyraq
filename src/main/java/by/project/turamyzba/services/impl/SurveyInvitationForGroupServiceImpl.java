package by.project.turamyzba.services.impl;

import by.project.turamyzba.entities.Group;
import by.project.turamyzba.entities.anketa.SurveyInvitationForGroup;
import by.project.turamyzba.exceptions.GroupNotFoundException;
import by.project.turamyzba.repositories.GroupRepository;
import by.project.turamyzba.repositories.anketa.SurveyInvitationForGroupRepository;
import by.project.turamyzba.repositories.anketa.SurveyInvitationRepository;
import by.project.turamyzba.services.SurveyInvitationForGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyInvitationForGroupServiceImpl implements SurveyInvitationForGroupService {
    private final GroupRepository groupRepository;
    private final SurveyInvitationForGroupRepository surveyInvitationForGroupRepository;
    private final SurveyInvitationRepository surveyInvitationRepository;
    @Override
    @Transactional
    public String createInvitationForGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found!"));
        SurveyInvitationForGroup invitation = new SurveyInvitationForGroup();
        invitation.setGroup(group);
        invitation.setToken(generateUniqueToken());
        invitation.setCreatedAt(LocalDateTime.now());
        surveyInvitationForGroupRepository.save(invitation);
        return invitation.getToken();
    }

    private String generateUniqueToken() {
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (surveyInvitationForGroupRepository.existsByToken(token) && surveyInvitationRepository.existsByToken(token));
        return token;
    }
}
