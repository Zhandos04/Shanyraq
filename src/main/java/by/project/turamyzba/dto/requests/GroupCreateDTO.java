package by.project.turamyzba.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class GroupCreateDTO {
    private Integer capacity;
    private Integer countOfPeople;
    private ResidentDataRequest memberData;
}
