package by.project.turamyzba.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementResponse {
    private Long id;

    private String role;

    private String title;

    private String selectedGender;

    private Boolean doYouLiveInThisHouse;

    private String howManyPeopleLiveInThisApartment;

    private Integer numberOfPeopleAreYouAccommodating;

    private Integer minAge;

    private Integer maxAge;

    private String region;

    private String district;

    private String microDistrict;

    private String address;

    private LocalDate arriveDate;

    private Integer cost;

    private String quantityOfRooms;

    private Boolean isDepositRequired;

    private Integer deposit;

    private Boolean arePetsAllowed;

    private Boolean isCommunalServiceIncluded;

    private Integer minAmountOfCommunalService;

    private Integer maxAmountOfCommunalService;

    private Boolean intendedForStudents;

    private Boolean areBadHabitsAllowed;

    private String apartmentsInfo;

    private String typeOfHousing;

    private Integer numberOfFloor;

    private Integer maxFloorInTheBuilding;

    private Integer areaOfTheApartment;

    private Boolean forALongTime;

    private String ownersName;

    private List<String> ownersPhoneNumbers;

    private String phoneNumber;

    private List<String> preferences;

    private String coordsX;

    private String coordsY;

    private List<ImageResponse> photos;

    private UserResponse user;

    private Boolean consideringOnlyNPeople;

    private Map<String, List<String>> residentsDataResponse;

    private List<ResidentResponseDTO> residentsSurvey;

    private List<SurveyAnswerDTO> creatorSurveyAnswers;
}
