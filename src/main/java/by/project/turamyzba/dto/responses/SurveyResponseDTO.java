package by.project.turamyzba.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class SurveyResponseDTO {
    private String fullName;
    private List<SurveyAnswerDTO> answers;
}
