package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.ProfileDTO;
import by.project.turamyzba.services.ProfileService;
import by.project.turamyzba.util.IncorrectJSONException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@Tag(name="Profile", description="Взаймодействие с профильем")
@CrossOrigin(origins = "*")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ProfileDTO get() {
        return profileService.getUser();
    }
    @PutMapping("/edit")
    public HttpStatus edit(@RequestBody @Valid ProfileDTO profileDTO, BindingResult bindingResult) throws IncorrectJSONException {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder str = new StringBuilder();
            for (FieldError error : errors) {
                str.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(";\n");
            }
            throw new IncorrectJSONException(str.toString());
        }
        profileService.editProfile(profileDTO);
        return HttpStatus.OK;
    }
}
