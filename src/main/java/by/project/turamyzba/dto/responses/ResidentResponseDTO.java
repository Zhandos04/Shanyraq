package by.project.turamyzba.dto.responses;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResidentResponseDTO {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String gender;
    private List<SurveyAnswerDTO> surveyAnswer;
}
