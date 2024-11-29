package by.project.turamyzba.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementFilterRequest {
    private String selectedGender; // гендер
    private String region;
    private String district;
    private String microDistrict;
    private String residentialComplex;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer numberOfPeopleAreYouAccommodating;
    private String quantityOfRooms;
    private Integer minAge;
    private Integer maxAge;
    private LocalDate arriveDate;
    private Integer minArea;
    private Integer maxArea;
    private Boolean notTheFirstFloor;
    private Boolean notTheTopFloor;
    private Boolean arePetsAllowed;
    private Boolean isCommunalServiceIncluded;
    private Boolean intendedForStudents;
    private Boolean onlyApartmentsWithoutResidents;
    private String fromWhom; // от хозяев - от жителей
    private String typeOfHousing; // дом - квартира
}
