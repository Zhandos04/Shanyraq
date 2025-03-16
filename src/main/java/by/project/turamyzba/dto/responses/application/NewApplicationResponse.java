package by.project.turamyzba.dto.responses.application;

import by.project.turamyzba.dto.responses.SurveyAnswerDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class NewApplicationResponse {
    private String email;
    private String name;
    private Integer age;
    private List<String> phoneNumbers;
    private LocalDate applicationDate;
    private List<SurveyAnswerDTO> answers;
}
