package by.project.turamyzba.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDTO {
    @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+", message = "fullname")
    private String fullName;
    private String nickName;
}
