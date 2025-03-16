package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.ResidentDataRequest;
import by.project.turamyzba.dto.requests.SubmitApplicationDTO;
import by.project.turamyzba.dto.responses.LinkForSurveyDTO;
import by.project.turamyzba.entities.Group;
import by.project.turamyzba.entities.GroupMember;
import by.project.turamyzba.entities.GroupMemberStatus;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.exceptions.GroupNotFoundException;
import by.project.turamyzba.repositories.GroupMemberRepository;
import by.project.turamyzba.repositories.GroupRepository;
import by.project.turamyzba.services.ApplicationService;
import by.project.turamyzba.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupMemberRepository groupMemberRepository;
    @Override
    @Transactional
    public LinkForSurveyDTO createApplication(Long groupId, SubmitApplicationDTO submitApplicationDTO) {
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

            groupMemberRepository.save(groupMember);
        } else {
            List<GroupMember> groupMembers = group.getMembers();
            int count = (int) groupMembers.stream()
                    .filter(groupMember -> groupMember.getStatus().equals(GroupMemberStatus.APPROVED))
                    .count();
            if (group.getCapacity() - count < submitApplicationDTO.getCountOfPeople()) {
                throw new IllegalArgumentException("Извините, но количество свободных мест в группе недостаточно");
            }

            for (ResidentDataRequest applicants : submitApplicationDTO.getMemberData()) {

            }
        }
        return null;
    }
}
