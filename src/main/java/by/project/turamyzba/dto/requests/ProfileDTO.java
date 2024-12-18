package by.project.turamyzba.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "FirstName должно начинаться с заглавной буквы и содержать только маленькие буквы после первой")
    private String firstName;
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "LastName должно начинаться с заглавной буквы и содержать только маленькие буквы после первой")
    private String lastName;
    @NotBlank(message = "Birth date cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Invalid date format. Expected yyyy-MM-dd")
    private String birthDate;
    private String phoneNumber;
    private String gender;
}
