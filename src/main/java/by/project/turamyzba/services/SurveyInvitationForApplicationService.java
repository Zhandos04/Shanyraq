package by.project.turamyzba.services;

import java.util.List;

public interface SurveyInvitationForApplicationService {
    String createInvitationForApplication(String applicationBatchId);
    List<String> getNamesFromToken(String token);
}
