package by.project.turamyzba.dto.responses;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AnnouncementResponseForAll {
    private Long announcementId;
    private String image;
    private String title;
    private String address;
    private LocalDate arriveDate;
    private String roomCount;
    private String selectedGender;
    private Integer roommates;
    private Integer cost;
}
