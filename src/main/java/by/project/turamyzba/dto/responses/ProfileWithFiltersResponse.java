package by.project.turamyzba.dto.responses;

import by.project.turamyzba.dto.requests.SavedFilterDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProfileWithFiltersResponse {
    private ProfileResponse profile;
    private List<SavedFilterDTO> savedFilters;
}

