package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.ProfileDTO;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.entities.usermodelenums.Gender;
import by.project.turamyzba.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {
    private final UserServiceImpl userService;


    @Override
    public ProfileDTO getUser() {
        String email = userService.getCurrentUser().getUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return convertToProfileDTO(user);
    }

    @Override
    @Transactional
    public void editProfile(ProfileDTO profileDTO) {
        String email = userService.getCurrentUser().getUsername();
        User updatedUser = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        updateUserData(updatedUser, profileDTO);
        userService.updateProfile(updatedUser);
    }

    private ProfileDTO convertToProfileDTO(User user) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setPhoneNumber(user.getPhoneNumber());
        profileDTO.setGender(user.getGender().name());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());
        profileDTO.setBirthDate(String.valueOf(user.getBirthDate()));

        return profileDTO;
    }

    private void updateUserData(User user, ProfileDTO profileDTO) {
        user.setEmail(profileDTO.getEmail());
        user.setGender(Gender.valueOf(profileDTO.getGender()));
        user.setBirthDate(LocalDate.parse(profileDTO.getBirthDate()));
        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        user.setPhoneNumber(profileDTO.getPhoneNumber());
        user.setUpdatedAt(Instant.now());
    }
}
