package by.project.turamyzba.services;


import by.project.turamyzba.dto.requests.AnketaDTO;

import java.util.List;

public interface AnketaService {
    void saveAnswers(List<AnketaDTO> anketaDTO);
    List<AnketaDTO> getAnswers();
}
