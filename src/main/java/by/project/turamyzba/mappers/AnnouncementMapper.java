package by.project.turamyzba.mappers;

import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.ImageResponse;
import by.project.turamyzba.dto.responses.UserResponse;
import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.Image;
import by.project.turamyzba.entities.User;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementMapper {

    public static Announcement toEntity(AnnouncementRequest request, String[] coords) {
        return Announcement.builder()
                .title(request.getTitle())
                .selectedGender(request.getSelectedGender())
                .doYouLiveInThisHouse(request.getDoYouLiveInThisHouse())
                .howManyPeopleLiveInThisApartment(request.getHowManyPeopleLiveInThisApartment())
                .numberOfPeopleAreYouAccommodating(request.getNumberOfPeopleAreYouAccommodating())
                .minAge(request.getMinAge())
                .maxAge(request.getMaxAge())
                .region(request.getRegion())
                .district(request.getDistrict())
                .microDistrict(request.getMicroDistrict())
                .address(request.getAddress())
                .arriveDate(request.getArriveDate())
                .cost(request.getCost())
                .quantityOfRooms(request.getQuantityOfRooms())
                .isDepositRequired(request.getIsDepositRequired())
                .deposit(request.getDeposit())
                .arePetsAllowed(request.getArePetsAllowed())
                .isCommunalServiceIncluded(request.getIsCommunalServiceIncluded())
                .minAmountOfCommunalService(request.getMinAmountOfCommunalService())
                .maxAmountOfCommunalService(request.getMaxAmountOfCommunalService())
                .intendedForStudents(request.getIntendedForStudents())
                .areBadHabitsAllowed(request.getAreBadHabitsAllowed())
                .apartmentsInfo(request.getApartmentsInfo())
                .typeOfHousing(request.getTypeOfHousing())
                .numberOfFloor(request.getNumberOfFloor())
                .maxFloorInTheBuilding(request.getMaxFloorInTheBuilding())
                .areaOfTheApartment(request.getAreaOfTheApartment())
                .residentialComplex(request.getResidentialComplex())
                .intersectionWith(request.getIntersectionWith())
                .forALongTime(request.getForALongTime())
                .ownersName(request.getOwnersName())
                .phoneNumbers(request.getPhoneNumbers()) // Список телефонов
                .residents(request.getResidents()) // Map "имя - телефон"
                .preferences(request.getPreferences()) // Список предпочтений
                .coordsX(coords != null && coords.length > 0 ? coords[0] : null) // Проверяем, что координаты не пустые
                .coordsY(coords != null && coords.length > 1 ? coords[1] : null) // Проверяем, что координаты не пустые
                .isDeleted(false) // Устанавливаем флаг удаления в false
                .isArchived(false) // По умолчанию архивным не является
                .build();
    }


    public static List<Image> toImages(List<String> imageUrls, Announcement announcement) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return new ArrayList<>();
        }

        List<Image> images = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            images.add(Image.builder()
                    .url(imageUrl)
                    .announcement(announcement)
                    .build());
        }
        return images;
    }

    public static ImageResponse toImageResponse(Image image) {
        return ImageResponse.builder()
                .url(image.getUrl())
                .id(image.getId())
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }


    public static void updateAnnouncementFromRequest(Announcement announcement, AnnouncementRequest request) {
        announcement.setTitle(request.getTitle());
        announcement.setSelectedGender(request.getSelectedGender());
        announcement.setDoYouLiveInThisHouse(request.getDoYouLiveInThisHouse());
        announcement.setHowManyPeopleLiveInThisApartment(request.getHowManyPeopleLiveInThisApartment());
        announcement.setNumberOfPeopleAreYouAccommodating(request.getNumberOfPeopleAreYouAccommodating());
        announcement.setMinAge(request.getMinAge());
        announcement.setMaxAge(request.getMaxAge());
        announcement.setRegion(request.getRegion());
        announcement.setDistrict(request.getDistrict());
        announcement.setMicroDistrict(request.getMicroDistrict());
        announcement.setAddress(request.getAddress());
        announcement.setArriveDate(request.getArriveDate());
        announcement.setQuantityOfRooms(request.getQuantityOfRooms());
        announcement.setIsDepositRequired(request.getIsDepositRequired());
        announcement.setDeposit(request.getDeposit());
        announcement.setArePetsAllowed(request.getArePetsAllowed());
        announcement.setIsCommunalServiceIncluded(request.getIsCommunalServiceIncluded());
        announcement.setMinAmountOfCommunalService(request.getMinAmountOfCommunalService());
        announcement.setMaxAmountOfCommunalService(request.getMaxAmountOfCommunalService());
        announcement.setIntendedForStudents(request.getIntendedForStudents());
        announcement.setAreBadHabitsAllowed(request.getAreBadHabitsAllowed());
        announcement.setApartmentsInfo(request.getApartmentsInfo());
        announcement.setTypeOfHousing(request.getTypeOfHousing());
        announcement.setNumberOfFloor(request.getNumberOfFloor());
        announcement.setMaxFloorInTheBuilding(request.getMaxFloorInTheBuilding());
        announcement.setAreaOfTheApartment(request.getAreaOfTheApartment());
        announcement.setResidentialComplex(request.getResidentialComplex());
        announcement.setIntersectionWith(request.getIntersectionWith());
        announcement.setForALongTime(request.getForALongTime());
        announcement.setOwnersName(request.getOwnersName());
        announcement.setPhoneNumbers(request.getPhoneNumbers());
        announcement.setResidents(request.getResidents());
        announcement.setPreferences(request.getPreferences());
    }
}
