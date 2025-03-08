package by.project.turamyzba.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class ProfileWithFiltersResponse {
    private ProfileResponse profile;
    private List<SavedFilterResponseDTO> savedFilters;
}

