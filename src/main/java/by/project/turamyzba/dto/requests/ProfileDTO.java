package by.project.turamyzba.dto.requests;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {
    @Pattern(
            regexp = "^$|^[A-Z][a-z]*$",
            message = "FirstName должно начинаться с заглавной буквы и содержать только маленькие буквы после первой"
    )
    private String firstName;

    @Pattern(
            regexp = "^$|^[A-Z][a-z]*$",
            message = "LastName должно начинаться с заглавной буквы и содержать только маленькие буквы после первой"
    )
    private String lastName;

    @Pattern(
            regexp = "^$|\\d{4}-\\d{2}-\\d{2}",
            message = "Invalid date format. Expected yyyy-MM-dd"
    )
    private String birthDate;

    @Pattern(
            regexp = "^$|^(\\+7)\\d{10}$",
            message = "Phone number должен соответствовать формату +7XXXXXXXXXX"
    )
    private String phoneNumber;

    @Pattern(
            regexp = "^$|^(Мужчина|Женщина|Любой)$",
            message = "Gender должно быть 'Мужчина', 'Женщина' или 'Любой'"
    )
    private String gender;
}
