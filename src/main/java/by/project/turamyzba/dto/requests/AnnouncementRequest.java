package by.project.turamyzba.dto.requests;

import jakarta.validation.constraints.*;
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
public class AnnouncementRequest {

    private String role;

    @NotNull(message = "Title cannot be null")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotNull(message = "Selected gender cannot be null")
    @Pattern(regexp = "Мужчина|Женщина|Любой", message = "Gender must be either Мужчина, Женщина or Любой")
    private String selectedGender;

    private Boolean doYouLiveInThisHouse;

    private String howManyPeopleLiveInThisApartment;

    private Integer numberOfPeopleAreYouAccommodating;

    private Integer minAge;

    private Integer maxAge;

    private String region;

    private String district;

    private String microDistrict;

    @NotNull(message = "Address cannot be null")
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    private String address;

    private LocalDate arriveDate;

    private Integer cost;

    private String quantityOfRooms;

    private Boolean isDepositRequired;

    private Integer deposit;

    private Boolean arePetsAllowed;

    @NotNull(message = "Communal service inclusion status cannot be null")
    private Boolean isCommunalServiceIncluded;

    private Integer minAmountOfCommunalService;

    private Integer maxAmountOfCommunalService;

    private Boolean intendedForStudents;

    private Boolean areBadHabitsAllowed;

    @NotNull(message = "Apartments info cannot be null")
    @Size(min = 10, max = 500, message = "Apartments info must be between 10 and 500 characters")
    private String apartmentsInfo;

    private List<String> images;

    private String typeOfHousing;

    private Integer numberOfFloor;

    private Integer maxFloorInTheBuilding;

    private Integer areaOfTheApartment;

    private Boolean forALongTime;
    @Pattern(
            regexp = "^$|^(\\+7)\\d{10}$",
            message = "Phone number должен соответствовать формату +7XXXXXXXXXX"
    )

    private String ownersName;

    private List<String> ownersPhoneNumbers;

    private Map<String, List<String>> residentsData;

    private List<String> preferences;

    private Boolean consideringOnlyNPeople;
}
