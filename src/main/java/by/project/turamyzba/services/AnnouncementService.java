package by.project.turamyzba.services;


import by.project.turamyzba.dto.requests.AnnouncementFilterRequest;
import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.entities.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface AnnouncementService {
    void createAnnouncement(AnnouncementRequest announcementRequest) throws IOException;
    AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest announcementRequest);

    Page<Announcement> getAllRoommateListings(Pageable pageable);

    Page<Announcement> searchRoommateListings(String search, Pageable pageable);

    Announcement getAnnouncementById(Long id);

    List<Announcement> getFilteredAnnouncements(AnnouncementFilterRequest request);

    List<Announcement> getUserAnnouncements(Long userId);

    AnnouncementResponse archiveAnnouncement(Long announcementId);

    AnnouncementResponse restoreAnnouncement(Long announcementId);

    void deleteAnnouncement(Long announcementId);
}
