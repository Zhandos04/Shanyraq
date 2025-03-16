package by.project.turamyzba.services;

import by.project.turamyzba.dto.requests.SubmitApplicationDTO;
import by.project.turamyzba.dto.responses.LinkForSurveyDTO;

public interface ApplicationService {
    LinkForSurveyDTO createApplication(Long groupId, SubmitApplicationDTO submitApplicationDTO);
}
