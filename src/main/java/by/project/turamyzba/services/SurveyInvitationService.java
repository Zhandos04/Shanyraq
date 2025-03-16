package by.project.turamyzba.services;

import java.util.List;

public interface SurveyInvitationService {
    String createInvitationForAnnouncement(Long announcementId);
    List<String> getNamesFromToken(String token);
}
