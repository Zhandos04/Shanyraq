package by.project.turamyzba.services;


import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.models.Announcement;

public interface AnnouncementService {
    AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest);
    AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest announcementRequest);
}
