package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.GroupCreateDTO;
import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.Group;
import by.project.turamyzba.entities.GroupMember;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.exceptions.AnnouncementNotFoundException;
import by.project.turamyzba.repositories.AnnouncementRepository;
import by.project.turamyzba.repositories.GroupMemberRepository;
import by.project.turamyzba.repositories.GroupRepository;
import by.project.turamyzba.services.GroupService;
import by.project.turamyzba.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserService userService;
    private final GroupMemberRepository groupMemberRepository;
    @Override
    @Transactional
    public void createGroup(Long announcementId, GroupCreateDTO groupCreateDTO) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new AnnouncementNotFoundException("Объявление не найдено"));
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        Group group = new Group();
        group.setCreatedAt(LocalDateTime.now());
        group.setName(groupCreateDTO.getName());
        group.setAnnouncement(announcement);
        group.setCapacity(group.getCapacity());
        group.setCreatedBy(user);

        Group savedGroup = groupRepository.save(group);

        GroupMember creatorMember = new GroupMember();
        creatorMember.setGroup(savedGroup);
        creatorMember.setUser(user);
        creatorMember.setJoinedAt(LocalDateTime.now());

        groupMemberRepository.save(creatorMember);
    }
}
