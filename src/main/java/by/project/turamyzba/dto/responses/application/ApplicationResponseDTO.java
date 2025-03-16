package by.project.turamyzba.dto.responses.application;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationResponseDTO {
    private String address;
    private String region;
    private String district;
    private String microDistrict;
    private String quantityOfRooms;
    private Integer areaOfTheApartment;
    private Integer numberOfFloor;
    private Integer maxFloorInTheBuilding;
    private Integer deposit;
    private String title;
    private Integer cost;

    private List<ApplicationForAnnouncementDTO> applications;
}
