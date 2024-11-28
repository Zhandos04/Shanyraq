package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.PasswordDTO;
import by.project.turamyzba.dto.requests.ProfileDTO;
import by.project.turamyzba.services.ProfileService;
import by.project.turamyzba.services.UserService;
import by.project.turamyzba.exceptions.IncorrectJSONException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@Tag(name="Profile", description="Взаймодействие с профильем")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    @PostMapping("/role")
    public ResponseEntity<?> chooseRole(@RequestParam String role) {
        userService.chooseRole(role);
        return ResponseEntity.ok("Role is choosen!");
    }
    @GetMapping
    @Operation(summary = "Register a new user", description = "Registers a new user by checking for existing IDs and phone numbers.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved profile")
    @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource")
    @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden")
    @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    public ProfileDTO get() {
        return profileService.getUser();
    }


    @PutMapping("/edit")
    @Operation(summary = "Edit user profile", description = "Updates the user profile based on provided data")
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "500", description = "Internal server error")
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

    @PatchMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordDTO passwordDTO) {
        profileService.updatePassword(passwordDTO);
        return ResponseEntity.ok("Пароль успешно изменен");
    }
}
