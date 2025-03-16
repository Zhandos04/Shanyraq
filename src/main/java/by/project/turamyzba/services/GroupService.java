package by.project.turamyzba.services;

import by.project.turamyzba.dto.requests.GroupCreateDTO;
import by.project.turamyzba.dto.responses.LinkForSurveyDTO;

public interface GroupService {
    LinkForSurveyDTO createGroup(Long announcementId, GroupCreateDTO groupCreateDTO);
}
