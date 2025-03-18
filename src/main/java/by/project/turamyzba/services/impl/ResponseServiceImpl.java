package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.responses.SurveyAnswerDTO;
import by.project.turamyzba.dto.responses.application.*;
import by.project.turamyzba.entities.*;
import by.project.turamyzba.exceptions.AnnouncementNotFoundException;
import by.project.turamyzba.repositories.*;
import by.project.turamyzba.repositories.anketa.UserAnswerRepository;
import by.project.turamyzba.services.ResponseService;
import by.project.turamyzba.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResponseServiceImpl implements ResponseService {
    private final AnnouncementRepository announcementRepository;
    private final GroupRepository groupRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final ResponseRepository responseRepository;
    private final ApplicationResponseRepository applicationResponseRepository;
    private final ApplicationRepository applicationRepository;
    private final UserService userService;

    @Override
    public ApplicationResponseDTO getAllResponsesForMyAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new AnnouncementNotFoundException("Announcement not found!"));

        User currentUser = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (announcement.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("It's not your announcement!");
        }

        List<Group> groups = groupRepository.findAllByAnnouncement(announcement);

        ApplicationResponseDTO answer = new ApplicationResponseDTO();

        answer.setAddress(announcement.getAddress());
        answer.setCost(announcement.getCost());
        answer.setDeposit(announcement.getDeposit());
        answer.setDistrict(announcement.getDistrict());
        answer.setRegion(announcement.getRegion());
        answer.setTitle(announcement.getTitle());
        answer.setMicroDistrict(announcement.getMicroDistrict());
        answer.setAreaOfTheApartment(announcement.getAreaOfTheApartment());
        answer.setQuantityOfRooms(announcement.getQuantityOfRooms());
        answer.setNumberOfFloor(announcement.getNumberOfFloor());
        answer.setMaxFloorInTheBuilding(announcement.getMaxFloorInTheBuilding());

        List<ApplicationForAnnouncementDTO> applications = new ArrayList<>();

        List<GroupMemberResponse> groupMemberResponses = new ArrayList<>();
        List<NewApplicationResponse> newApplicationResponses = new ArrayList<>();
        List<NewApplicationWithPeopleResponse> newApplicationWithPeopleResponses = new ArrayList<>();

        for (Group group : groups) {
            Optional<Response> optionalResponse = responseRepository.findByAnnouncementAndGroup(announcement, group);
            if (optionalResponse.isPresent() && optionalResponse.get().getStatus().equals(ResponseStatus.APPROVED)) {
                List<GroupMember> groupMembers = group.getMembers();

                GroupMemberResponse groupMemberResponse = new GroupMemberResponse();
                NewApplicationResponse newApplicationResponse = new NewApplicationResponse();
                for (GroupMember groupMember : groupMembers) {
                    if (groupMember.getStatus().equals(GroupMemberStatus.APPROVED)) {
                        groupMemberResponse.setName(groupMember.getName());
                        groupMemberResponse.setJoinedDate(groupMember.getJoinedAt().toLocalDate());
                        groupMemberResponse.setEmail(groupMember.getUser().getEmail());
                        groupMemberResponse.setPhoneNumbers(groupMember.getPhoneNumbers());
                        groupMemberResponse.setAge(groupMember.getAge());

                        List<SurveyAnswerDTO> userAnswers = userAnswerRepository.findAllByUser(groupMember.getUser())
                                .stream()
                                .map(userAnswer -> {
                                    SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
                                    surveyAnswerDTO.setQuestion(userAnswer.getQuestion().getText());
                                    surveyAnswerDTO.setAnswer(userAnswer.getOption().getText());
                                    return surveyAnswerDTO;
                                })
                                .toList();

                        groupMemberResponse.setAnswers(userAnswers);
                        groupMemberResponses.add(groupMemberResponse);
                    } else if (groupMember.getStatus().equals(GroupMemberStatus.PENDING)) {
                        newApplicationResponse.setName(groupMember.getName());
                        newApplicationResponse.setApplicationDate(groupMember.getAppliedDate().toLocalDate());
                        newApplicationResponse.setEmail(groupMember.getUser().getEmail());
                        newApplicationResponse.setPhoneNumbers(groupMember.getPhoneNumbers());
                        newApplicationResponse.setAge(groupMember.getAge());

                        List<SurveyAnswerDTO> userAnswers = userAnswerRepository.findAllByUser(groupMember.getUser())
                                .stream()
                                .map(userAnswer -> {
                                    SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
                                    surveyAnswerDTO.setQuestion(userAnswer.getQuestion().getText());
                                    surveyAnswerDTO.setAnswer(userAnswer.getOption().getText());
                                    return surveyAnswerDTO;
                                })
                                .toList();

                        newApplicationResponse.setAnswers(userAnswers);
                        newApplicationResponses.add(newApplicationResponse);
                    }
                }

                NewApplicationWithPeopleResponse newApplicationWithPeopleResponse = new NewApplicationWithPeopleResponse();

                List<ApplicationResponse> applicationResponses = applicationResponseRepository.findAllByGroup(group);
                for (ApplicationResponse applicationResponse : applicationResponses) {
                    List<EachPersonInNewApplicationWithPeopleResponse> people = new ArrayList<>();
                    List<Application> applications1 = applicationRepository.findAllByApplicationBatchId(applicationResponse.getApplicationBatchId());
                    for (Application application : applications1) {
                        EachPersonInNewApplicationWithPeopleResponse person = new EachPersonInNewApplicationWithPeopleResponse();
                        person.setAge(application.getAge());
                        person.setName(application.getName());
                        person.setEmail(application.getUser().getEmail());
                        person.setPhoneNumbers(application.getPhoneNumbers());
                        person.setApplicationDate(application.getAppliedDate().toLocalDate());

                        List<SurveyAnswerDTO> userAnswers = userAnswerRepository.findAllByUser(application.getUser())
                                .stream()
                                .map(userAnswer -> {
                                    SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
                                    surveyAnswerDTO.setQuestion(userAnswer.getQuestion().getText());
                                    surveyAnswerDTO.setAnswer(userAnswer.getOption().getText());
                                    return surveyAnswerDTO;
                                })
                                .toList();

                        person.setAnswers(userAnswers);
                        people.add(person);
                    }
                    newApplicationWithPeopleResponse.setPeople(people);
                    newApplicationWithPeopleResponses.add(newApplicationWithPeopleResponse);
                }
                ApplicationForAnnouncementDTO application = new ApplicationForAnnouncementDTO();
                application.setNewApplicationsWithPeople(newApplicationWithPeopleResponses);
                application.setGroupMembers(groupMemberResponses);
                application.setNewApplications(newApplicationResponses);
                application.setCapacityOfGroup(group.getCapacity());
                application.setIsApproved(true);
                applications.add(application);
            } else if (optionalResponse.isPresent() && optionalResponse.get().getStatus().equals(ResponseStatus.PENDING)) {
                List<GroupMember> groupMembers = group.getMembers();

                GroupMemberResponse groupMemberResponse = new GroupMemberResponse();
                for (GroupMember groupMember : groupMembers) {
                    groupMemberResponse.setName(groupMember.getName());
                    groupMemberResponse.setJoinedDate(groupMember.getJoinedAt().toLocalDate());
                    groupMemberResponse.setEmail(groupMember.getUser().getEmail());
                    groupMemberResponse.setPhoneNumbers(groupMember.getPhoneNumbers());
                    groupMemberResponse.setAge(groupMember.getAge());
                    List<SurveyAnswerDTO> userAnswers = userAnswerRepository.findAllByUser(groupMember.getUser())
                            .stream()
                            .map(userAnswer -> {
                                SurveyAnswerDTO surveyAnswerDTO = new SurveyAnswerDTO();
                                surveyAnswerDTO.setQuestion(userAnswer.getQuestion().getText());
                                surveyAnswerDTO.setAnswer(userAnswer.getOption().getText());
                                return surveyAnswerDTO;
                            })
                            .toList();
                    groupMemberResponse.setAnswers(userAnswers);
                    groupMemberResponses.add(groupMemberResponse);
                }
                ApplicationForAnnouncementDTO application = new ApplicationForAnnouncementDTO();
                application.setNewApplicationsWithPeople(newApplicationWithPeopleResponses);
                application.setGroupMembers(groupMemberResponses);
                application.setNewApplications(newApplicationResponses);
                application.setCapacityOfGroup(group.getCapacity());
                application.setIsApproved(false);
                applications.add(application);
            }
        }
        answer.setApplications(applications);

        return answer;
    }
}
