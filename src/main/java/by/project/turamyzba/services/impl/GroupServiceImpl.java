package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.GroupCreateDTO;
import by.project.turamyzba.dto.requests.ResidentDataRequest;
import by.project.turamyzba.dto.responses.LinkForSurveyDTO;
import by.project.turamyzba.entities.*;
import by.project.turamyzba.exceptions.AnnouncementNotFoundException;
import by.project.turamyzba.repositories.AnnouncementRepository;
import by.project.turamyzba.repositories.GroupMemberRepository;
import by.project.turamyzba.repositories.GroupRepository;
import by.project.turamyzba.repositories.ResponseRepository;
import by.project.turamyzba.services.GroupService;
import by.project.turamyzba.services.SurveyInvitationForGroupService;
import by.project.turamyzba.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserService userService;
    private final GroupMemberRepository groupMemberRepository;
    private final SurveyInvitationForGroupService surveyInvitationForGroupService;
    private final ResponseRepository responseRepository;

    @Override
    @Transactional
    public LinkForSurveyDTO createGroup(Long announcementId, GroupCreateDTO groupCreateDTO) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new AnnouncementNotFoundException("Объявление не найдено"));
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        Group group = new Group();
        group.setCreatedAt(LocalDateTime.now());
        group.setAnnouncement(announcement);
        group.setCapacity(groupCreateDTO.getCapacity());
        group.setCreator(user);

        Group savedGroup = groupRepository.save(group);

        GroupMember creatorMember = new GroupMember();
        creatorMember.setName(user.getFirstName());
        creatorMember.setGroup(savedGroup);
        creatorMember.setAge(Period.between(user.getBirthDate(), LocalDate.now()).getYears());
        creatorMember.setUser(user);
        creatorMember.setJoinedAt(LocalDateTime.now());
        creatorMember.setStatus(GroupMemberStatus.APPROVED);

        groupMemberRepository.save(creatorMember);

        LinkForSurveyDTO link = new LinkForSurveyDTO();
        if (groupCreateDTO.getCountOfPeople() > 1) {
            for (ResidentDataRequest groupMembers : groupCreateDTO.getMemberData()) {
                GroupMember groupMember = new GroupMember();
                groupMember.setName(groupMembers.getName());
                groupMember.setPhoneNumbers(groupMembers.getPhoneNumbers());
                groupMember.setGroup(savedGroup);
                groupMember.setJoinedAt(LocalDateTime.now());
                groupMember.setStatus(GroupMemberStatus.PENDING);
                groupMemberRepository.save(groupMember);
            }
            link.setToken(surveyInvitationForGroupService.createInvitationForGroup(savedGroup.getId()));
            link.setMessage("Ссылка для анкеты создана");
        } else {
            Response response = new Response();
            response.setAnnouncement(group.getAnnouncement());
            response.setGroup(group);
            response.setStatus(ResponseStatus.PENDING);
            responseRepository.save(response);
            link.setToken(null);
            link.setMessage("Заявка на созданий группу отправлена");
        }
        return link;
    }
}
