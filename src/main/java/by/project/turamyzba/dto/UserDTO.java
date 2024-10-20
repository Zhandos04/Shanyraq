package by.project.turamyzba.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "FirstName должно начинаться с заглавной буквы и содержать только маленькие буквы после первой")
    private String firstName;

    @Pattern(regexp = "^[A-Z][a-z]*$", message = "LastName должно начинаться с заглавной буквы и содержать только маленькие буквы после первой")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Неверный формат email")
    private String email;

    private String nickName;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_#])(?=.*[a-z])[A-Za-z\\d@$!%*?&_#]{8,}$",
            message = "password")
    private String password;

//    @Pattern(regexp = "^((\\+7)\\d{10})$",
//            message = "phoneNumber")
//    private String phoneNumber;
}