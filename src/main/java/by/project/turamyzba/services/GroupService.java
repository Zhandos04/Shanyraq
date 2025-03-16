package by.project.turamyzba.services;

import by.project.turamyzba.dto.requests.GroupCreateDTO;
import by.project.turamyzba.dto.responses.LinkForSurveyDTO;

import java.util.List;

public interface GroupService {
    LinkForSurveyDTO createGroup(Long announcementId, GroupCreateDTO groupCreateDTO);
    List<String> getNamesFromToken(String token);
}
