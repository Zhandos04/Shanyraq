package by.project.turamyzba.services;


import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.dto.responses.AnnouncementResponseForAll;
import by.project.turamyzba.dto.responses.ResidentResponseDTO;
import by.project.turamyzba.entities.Announcement;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface AnnouncementService {
    Announcement createAnnouncement(AnnouncementRequest announcementRequest) throws IOException;
    Page<AnnouncementResponseForAll> getAllRoommateListings(Pageable pageable);
    Page<AnnouncementResponseForAll> searchRoommateListings(
            String region,
            String district,
            String microDistrict,
            Integer minPrice,
            Integer maxPrice,
            String gender,
            Integer roommatesCount,
            Pageable pageable);
    AnnouncementResponse getAnnouncementById(Long id);
    List<AnnouncementResponseForAll> getUserAnnouncements();
    List<AnnouncementResponseForAll> getUserArchiveAnnouncements();
    void archiveAnnouncement(Long announcementId) throws BadRequestException;
    AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest announcementRequest);
    List<Announcement> getFilteredAnnouncements(String selectedGender, String region, String district, String microDistrict,
                                                Integer minPrice, Integer maxPrice, Integer numberOfPeopleAreYouAccommodating,
                                                String quantityOfRooms, Integer minAge, Integer maxAge, String arriveData,
                                                Integer minArea, Integer maxArea, Boolean notTheFirstFloor, Boolean notTheTopFloor,
                                                Boolean arePetsAllowed, Boolean isCommunalServiceIncluded, Boolean intendedForStudents,
                                                String typeOfHousing, Boolean forALongTime, Boolean consideringOnlyNPeople);
    void restoreAnnouncement(Long announcementId) throws BadRequestException;
    void deleteAnnouncement(Long announcementId) throws BadRequestException;
    AnnouncementResponse toAnnouncementResponse(Announcement announcement);
    AnnouncementResponseForAll toAnnouncementResponseForAll(Announcement announcement);
    ResidentResponseDTO getSurveyAnswers(Long id, String name);
}
