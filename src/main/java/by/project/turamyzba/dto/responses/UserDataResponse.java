package by.project.turamyzba.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class UserDataResponse {
    private String firstName;
    private String birthDate;
    private String phoneNumber;
    private String gender;
    private List<SurveyAnswerDTO> surveyAnswers;
}
