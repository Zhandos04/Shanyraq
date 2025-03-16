package by.project.turamyzba.dto.requests;

import by.project.turamyzba.dto.responses.UserAnswerDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class SurveyFromLinkDTO {
    private String token;
    private String firstName;
    @Pattern(
            regexp = "^$|\\d{4}-\\d{2}-\\d{2}",
            message = "Invalid date format. Expected yyyy-MM-dd"
    )
    private String birthDate;
    private String phoneNumber;
    @Email
    private String email;
    private String gender;

    private List<UserAnswerDTO> userAnswers;
}
