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

    @Pattern(
            regexp = "^(?:$|(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_#.])(?=.*[a-z])[A-Za-z\\d@$!%*?&_#.]{8,})$",
            message = "Password должен содержать как минимум одну заглавную букву, одну цифру и один символ, и быть длиной не менее 8 символов"
    )
    private String password;


    private List<UserAnswerDTO> userAnswers;
}
