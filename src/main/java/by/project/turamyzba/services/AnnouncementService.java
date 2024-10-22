package by.project.turamyzba.services;


import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.models.Announcement;

public interface AnnouncementService {
    Announcement createAnnouncement(AnnouncementRequest announcementDTO);
    Announcement updateAnnouncement(Long id, AnnouncementRequest announcementDTO);
}
