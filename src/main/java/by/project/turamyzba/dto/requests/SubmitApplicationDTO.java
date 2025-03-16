package by.project.turamyzba.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class SubmitApplicationDTO {
    private Integer countOfPeople;
    private List<ResidentDataRequest> memberData;
}
