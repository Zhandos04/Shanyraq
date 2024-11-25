package by.project.turamyzba.mappers;

import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.Image;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementMapper {

    public static Announcement toEntity(AnnouncementRequest request, String[] coords) {
        return Announcement.builder()
                .title(request.getTitle())
                .apartmentsInfo(request.getApartmentsInfo())
                .address(request.getAddress())
                .coordsX(coords[0])
                .coordsY(coords[1])
                .deposit(request.getDeposit())
                .maxPeople(request.getMaxPeople())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .selectedGender(request.getSelectedGender())
                .isCommunalServiceIncluded(request.getIsCommunalServiceIncluded())
                .build();
    }

    public static List<Image> toImages(List<String> imageUrls, Announcement announcement) {
        List<Image> images = new ArrayList<>();
        if(imageUrls != null) for (String imageUrl : imageUrls) {
            images.add(Image.builder()
                    .url(imageUrl)
                    .announcement(announcement)
                    .build());
        }
        return images;
    }

    public static void updateAnnouncementFromRequest(Announcement announcement, AnnouncementRequest request) {
        announcement.setTitle(request.getTitle());
        announcement.setApartmentsInfo(request.getApartmentsInfo());
        announcement.setAddress(request.getAddress());
        announcement.setDeposit(request.getDeposit());
        announcement.setMaxPeople(request.getMaxPeople());
        announcement.setSelectedGender(request.getSelectedGender());
        announcement.setIsCommunalServiceIncluded(request.getIsCommunalServiceIncluded());
        List<Image> images = toImages(request.getImageUrls(), announcement);
        announcement.getPhotos().addAll(images);
    }
}
