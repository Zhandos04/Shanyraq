package by.project.turamyzba.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementRequest {
    private String title;
    private String apartmentsInfo;
    private String address;
    private String startAt;
    private Integer deposit;
    private Integer maxPeople;
    private String selectedGender;
    private Boolean isCommunalServiceIncluded;
    private String roomiePreferences;
    private Integer monthlyExpensePerPerson;
    private List<String> imageUrls;
}
