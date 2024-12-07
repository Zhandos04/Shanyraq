package by.project.turamyzba.dto.responses;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private String coordsX;
    private String coordsY;
}
