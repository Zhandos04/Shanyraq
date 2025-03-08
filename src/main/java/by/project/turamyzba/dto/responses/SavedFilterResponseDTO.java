package by.project.turamyzba.dto.responses;

import lombok.Data;

@Data
public class SavedFilterResponseDTO {
    private Long id;
    private String selectedGender;
    private String region;
    private String district;
    private String microDistrict;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer numberOfPeopleAreYouAccommodating;
    private String quantityOfRooms;
    private Integer minAge;
    private Integer maxAge;
    private String arriveDate;
    private Integer minArea;
    private Integer maxArea;
    private Boolean notTheFirstFloor;
    private Boolean notTheTopFloor;
    private Boolean arePetsAllowed;
    private Boolean isCommunalServiceIncluded;
    private Boolean intendedForStudents;
    private String typeOfHousing;
    private Boolean forALongTime;
}
