package by.project.turamyzba.dto.responses;

import lombok.Data;

@Data
public class ProfileResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private String phoneNumber;
    private String gender;
    private String profilePhoto;
    private Boolean isPasswordHas;
}
