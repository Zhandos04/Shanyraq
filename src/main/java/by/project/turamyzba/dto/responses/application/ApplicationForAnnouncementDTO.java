package by.project.turamyzba.dto.responses.application;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationForAnnouncementDTO {
    List<GroupMemberResponse> groupMembers;
    List<NewApplicationResponse> newApplications;
    private Integer capacityOfGroup;
    private Boolean isApproved;
}
