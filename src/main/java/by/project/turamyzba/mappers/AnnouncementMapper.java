package by.project.turamyzba.mappers;

import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.ImageResponse;
import by.project.turamyzba.dto.responses.UserResponse;
import by.project.turamyzba.entities.*;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementMapper {

    public static Announcement toEntity(AnnouncementRequest request, String[] coords) {
        List<ResidentData> residents = request.getResidentsData().entrySet().stream()
                .map(entry -> {
                    ResidentData resident = new ResidentData();
                    resident.setName(entry.getKey());
                    resident.setPhoneNumbers(entry.getValue());
                    return resident;
                })
                .toList();
        return Announcement.builder()
                .residentData(residents)
                .title(request.getTitle())
                .role(request.getRole())
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
                .forALongTime(request.getForALongTime())
                .ownersName(request.getOwnersName())
                .ownersPhoneNumbers(request.getOwnersPhoneNumbers())
                .preferences(request.getPreferences())
                .coordsX(coords != null && coords.length > 0 ? coords[0] : null)
                .coordsY(coords != null && coords.length > 1 ? coords[1] : null)
                .isDeleted(false)
                .isArchived(false)
                .consideringOnlyNPeople(request.getConsideringOnlyNPeople())
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
                .profilePhoto(user.getProfilePhoto())
                .build();
    }

    public static void updateAnnouncementFromRequest(Announcement announcement, AnnouncementRequest request) {
        announcement.setTitle(request.getTitle());
        announcement.setRole(request.getRole());
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
        announcement.setForALongTime(request.getForALongTime());
        announcement.setOwnersName(request.getOwnersName());
        List<ResidentData> residents = request.getResidentsData().entrySet().stream()
                .map(entry -> {
                    ResidentData resident = new ResidentData();
                    resident.setName(entry.getKey());
                    resident.setPhoneNumbers(entry.getValue());
                    return resident;
                })
                .toList();
        announcement.setResidentData(residents);
        announcement.setOwnersPhoneNumbers(request.getOwnersPhoneNumbers());
        announcement.setPreferences(request.getPreferences());
        announcement.setConsideringOnlyNPeople(request.getConsideringOnlyNPeople());
    }
}
