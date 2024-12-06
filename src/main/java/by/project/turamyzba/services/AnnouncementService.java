package by.project.turamyzba.services;


import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.dto.responses.AnnouncementResponseForAll;
import by.project.turamyzba.entities.Announcement;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface AnnouncementService {
    void createAnnouncement(AnnouncementRequest announcementRequest) throws IOException;
    Page<Announcement> getAllRoommateListings(Pageable pageable);
    Page<Announcement> searchRoommateListings(String region, String district, String microDistrict, Integer minPrice, Integer maxPrice, String gender, Integer roommatesCount, Pageable pageable);
    AnnouncementResponse getAnnouncementById(Long id);
    List<AnnouncementResponse> getUserAnnouncements();
    List<AnnouncementResponse> getUserArchiveAnnouncements();
    void archiveAnnouncement(Long announcementId) throws BadRequestException;
    AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest announcementRequest);
    List<Announcement> getFilteredAnnouncements(String selectedGender, String region, String district, String microDistrict,
                                                Integer minPrice, Integer maxPrice, Integer numberOfPeopleAreYouAccommodating,
                                                String quantityOfRooms, Integer minAge, Integer maxAge, LocalDate arriveData,
                                                Integer minArea, Integer maxArea, Boolean notTheFirstFloor, Boolean notTheTopFloor,
                                                Boolean arePetsAllowed, Boolean isCommunalServiceIncluded, Boolean intendedForStudents,
                                                String typeOfHousing, Boolean forALongTime);
    void restoreAnnouncement(Long announcementId) throws BadRequestException;
    void deleteAnnouncement(Long announcementId) throws BadRequestException;
    AnnouncementResponse toAnnouncementResponse(Announcement announcement);
    AnnouncementResponseForAll toAnnouncementResponseForAll(Announcement announcement);
}
