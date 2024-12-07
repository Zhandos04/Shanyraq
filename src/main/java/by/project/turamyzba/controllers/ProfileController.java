package by.project.turamyzba.controllers;

import by.project.turamyzba.dto.requests.AddPasswordRequest;
import by.project.turamyzba.dto.requests.PasswordDTO;
import by.project.turamyzba.dto.requests.ProfileDTO;
import by.project.turamyzba.dto.responses.ProfileResponse;
import by.project.turamyzba.exceptions.IncorrectJSONException;
import by.project.turamyzba.services.ProfileService;
import by.project.turamyzba.services.impl.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/profile")
@Tag(name="Profile", description="Взаймодействие с профильем")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final S3Service s3Service;

    @GetMapping
    @Operation(summary = "Профиль данные алу")
    public ProfileResponse get() {
        return profileService.getUser();
    }

    @PostMapping("add-password")
    @Operation(summary = "Гуглмен тиркелген пользовательге пароль енгизу")
    public ResponseEntity<?> addPassword(@RequestBody @Valid AddPasswordRequest addPasswordRequest) {
        profileService.addPassword(addPasswordRequest.getPassword());
        return ResponseEntity.ok("Password added successfully!");
    }


    @PutMapping("/edit")
    @Operation(summary = "Edit user profile", description = "Updates the user profile based on provided data")
    public ResponseEntity<ProfileResponse> edit(@RequestBody @Valid ProfileDTO profileDTO, BindingResult bindingResult) throws IncorrectJSONException {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder str = new StringBuilder();
            for (FieldError error : errors) {
                str.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(";\n");
            }
            throw new IncorrectJSONException(str.toString());
        }
        return ResponseEntity.ok(profileService.editProfile(profileDTO));
    }

    @PostMapping("/update-password")
    @Operation(summary = "Пароль озгерту")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordDTO passwordDTO) {
        profileService.updatePassword(passwordDTO);
        return ResponseEntity.ok("Пароль успешно изменен");
    }

    @PostMapping(value = "/upload-photo", consumes = "multipart/form-data")
    @Operation(summary = "Фото профилья")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file provided");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body("Invalid file type. Only image files are allowed.");
        }

        if (file.getSize() > 10 * 1024 * 1024) { // 10MB
            return ResponseEntity.badRequest().body("File is too large. Maximum size is 10MB.");
        }

        try {
            String fileUrl = s3Service.uploadFile(file);
            profileService.uploadProfilePhoto(fileUrl);

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }
}
