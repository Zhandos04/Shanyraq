package by.project.turamyzba.services;

import by.project.turamyzba.dto.requests.SavedFilterDTO;
import by.project.turamyzba.dto.responses.SavedFilterResponseDTO;

import java.util.List;

public interface SavedFilterService {
    void saveFilter(SavedFilterDTO filterDTO);
    List<SavedFilterResponseDTO> deleteFilter(Long id);
}

