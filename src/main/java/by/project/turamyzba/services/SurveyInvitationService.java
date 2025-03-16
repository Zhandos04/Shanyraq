package by.project.turamyzba.services;

import by.project.turamyzba.entities.anketa.SurveyInvitation;

import java.util.List;


public interface SurveyInvitationService {
    SurveyInvitation createInvitation(Long announcementId);
    List<String> getNamesFromToken(String token);
}
