package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.PasswordDTO;
import by.project.turamyzba.dto.requests.ProfileDTO;
import by.project.turamyzba.dto.responses.ProfileResponse;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.entities.usermodelenums.Gender;
import by.project.turamyzba.repositories.UserRepository;
import by.project.turamyzba.services.ProfileService;
import by.project.turamyzba.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public ProfileResponse getUser() {
        String email = userService.getCurrentUser().getUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return convertToProfileResponse(user);
    }

    @Override
    @Transactional
    public ProfileResponse editProfile(ProfileDTO profileDTO) {
        String email = userService.getCurrentUser().getUsername();
        User updatedUser = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        updateUserData(updatedUser, profileDTO);
        userService.updateProfile(updatedUser);
        return convertToProfileResponse(updatedUser);
    }

    @Override
    @Transactional
    public void updatePassword(PasswordDTO passwordDTO) {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        } else {
            throw new BadCredentialsException("Старый пароль неверный");
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void uploadProfilePhoto(String fileUrl) {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setProfilePhoto(fileUrl);
        userRepository.save(user);
    }

    private ProfileResponse convertToProfileResponse(User user) {
        ProfileResponse profileDTO = new ProfileResponse();
        profileDTO.setPhoneNumber(user.getPhoneNumber());
        profileDTO.setGender(user.getGender() != null ? user.getGender().name() : "Not Provided");
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());
        profileDTO.setBirthDate(String.valueOf(user.getBirthDate()));
        profileDTO.setProfilePhoto(user.getProfilePhoto());
        profileDTO.setEmail(user.getEmail());

        return profileDTO;
    }

    private void updateUserData(User user, ProfileDTO profileDTO) {
        if (profileDTO.getGender() != null && !profileDTO.getGender().isEmpty()) {
            user.setGender(Gender.valueOf(profileDTO.getGender().toUpperCase()));
        } else {
            // Handle the case where gender is missing, e.g., set default value
            user.setGender(Gender.NOT_PROVIDED); // Assuming NOT_PROVIDED is an enum value
        }
        user.setBirthDate(LocalDate.parse(profileDTO.getBirthDate()));
        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        user.setPhoneNumber(profileDTO.getPhoneNumber());
        user.setUpdatedAt(Instant.now());
    }
}
