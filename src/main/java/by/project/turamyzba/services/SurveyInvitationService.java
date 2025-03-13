package by.project.turamyzba.services;

import by.project.turamyzba.entities.anketa.SurveyInvitation;

public interface SurveyInvitationService {
    SurveyInvitation createInvitation(Long announcementId);
}
