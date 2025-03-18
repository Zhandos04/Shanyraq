package by.project.turamyzba.dto.responses;

import by.project.turamyzba.dto.responses.application.GroupMemberResponse;
import lombok.Data;

import java.util.List;

@Data
public class GroupDataResponseDTO {
    private List<GroupMemberResponse> groupMembers;
}
