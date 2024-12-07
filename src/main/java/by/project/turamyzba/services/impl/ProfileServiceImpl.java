package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.PasswordDTO;
import by.project.turamyzba.dto.requests.ProfileDTO;
import by.project.turamyzba.dto.responses.ProfileResponse;
import by.project.turamyzba.entities.User;
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
import java.time.format.DateTimeParseException;

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

    @Override
    @Transactional
    public void addPassword(String password) {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    private ProfileResponse convertToProfileResponse(User user) {
        ProfileResponse profileDTO = new ProfileResponse();
        profileDTO.setPhoneNumber(user.getPhoneNumber());
        profileDTO.setGender(user.getGender());
        profileDTO.setFirstName(user.getFirstName());
        profileDTO.setLastName(user.getLastName());
        if (user.getBirthDate() != null) {
            profileDTO.setBirthDate(user.getBirthDate().toString()); // ISO format by default
        } else {
            profileDTO.setBirthDate(""); // or set to null based on your requirements
        }
        profileDTO.setProfilePhoto(user.getProfilePhoto());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setIsPasswordHas(!user.getPassword().isEmpty());

        return profileDTO;
    }

    private void updateUserData(User user, ProfileDTO profileDTO) {
        user.setGender(profileDTO.getGender());
        String birthDateStr = profileDTO.getBirthDate();
        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            try {
                LocalDate birthDate = LocalDate.parse(birthDateStr);
                user.setBirthDate(birthDate);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid birthDate format: " + birthDateStr);
                throw new IllegalArgumentException("Invalid birthDate format.", e);
            }
        } else {
            user.setBirthDate(null);
        }
        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        if (profileDTO.getPhoneNumber().isEmpty()) user.setPhoneNumber(null);
        else user.setPhoneNumber(profileDTO.getPhoneNumber());
        user.setUpdatedAt(Instant.now());
    }
}
