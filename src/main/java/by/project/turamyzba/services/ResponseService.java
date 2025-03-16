package by.project.turamyzba.services;

import by.project.turamyzba.dto.responses.application.ApplicationResponseDTO;

public interface ResponseService {
    ApplicationResponseDTO getAllResponsesForMyAnnouncement(Long announcementId);
}
