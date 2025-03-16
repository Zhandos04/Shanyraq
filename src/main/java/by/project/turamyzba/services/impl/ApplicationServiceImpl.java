package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.ResidentDataRequest;
import by.project.turamyzba.dto.requests.SubmitApplicationDTO;
import by.project.turamyzba.dto.responses.LinkForSurveyDTO;
import by.project.turamyzba.entities.*;
import by.project.turamyzba.exceptions.GroupNotFoundException;
import by.project.turamyzba.repositories.ApplicationRepository;
import by.project.turamyzba.repositories.GroupMemberRepository;
import by.project.turamyzba.repositories.GroupRepository;
import by.project.turamyzba.services.ApplicationService;
import by.project.turamyzba.services.SurveyInvitationForApplicationService;
import by.project.turamyzba.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupMemberRepository groupMemberRepository;
    private final ApplicationRepository applicationRepository;
    private final SurveyInvitationForApplicationService surveyInvitationForApplicationService;
    @Override
    @Transactional
    public LinkForSurveyDTO createApplication(Long groupId, SubmitApplicationDTO submitApplicationDTO) {
        LinkForSurveyDTO linkForSurveyDTO = new LinkForSurveyDTO();
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found!"));

        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (submitApplicationDTO.getCountOfPeople() == 1) {
            GroupMember groupMember = new GroupMember();
            groupMember.setName(user.getFirstName());
            groupMember.setUser(user);
            groupMember.setGroup(group);
            groupMember.setAppliedDate(LocalDateTime.now());
            groupMember.setStatus(GroupMemberStatus.PENDING);
            groupMember.setAge(Period.between(user.getBirthDate(), LocalDate.now()).getYears());

            linkForSurveyDTO.setToken(null);
            linkForSurveyDTO.setMessage("Заявка в группу отправлена");
            groupMemberRepository.save(groupMember);
        } else {
            List<GroupMember> groupMembers = group.getMembers();
            int approvedCount = (int) groupMembers.stream()
                    .filter(groupMember -> groupMember.getStatus().equals(GroupMemberStatus.APPROVED))
                    .count();

            if (group.getCapacity() - approvedCount < submitApplicationDTO.getCountOfPeople()) {
                throw new IllegalArgumentException("Извините, но количество свободных мест в группе недостаточно");
            }

            String applicationBatchId = UUID.randomUUID().toString();

            Application mainApplicant = new Application();
            mainApplicant.setName(user.getFirstName());
            mainApplicant.setUser(user);
            mainApplicant.setGroup(group);
            mainApplicant.setAge(Period.between(user.getBirthDate(), LocalDate.now()).getYears());
            mainApplicant.setApplicationBatchId(applicationBatchId);
            mainApplicant.setAppliedDate(LocalDateTime.now());

            applicationRepository.save(mainApplicant);

            for (ResidentDataRequest applicantData : submitApplicationDTO.getMemberData()) {
                Application friendApplicant = new Application();
                friendApplicant.setName(applicantData.getName());
                friendApplicant.setGroup(group);
                friendApplicant.setPhoneNumbers(applicantData.getPhoneNumbers());
                friendApplicant.setApplicationBatchId(applicationBatchId);

                applicationRepository.save(friendApplicant);
            }
            linkForSurveyDTO.setToken(surveyInvitationForApplicationService.createInvitationForApplication(applicationBatchId));
            linkForSurveyDTO.setMessage("Ссылка для анкеты создана");
        }
        return linkForSurveyDTO;
    }
}
