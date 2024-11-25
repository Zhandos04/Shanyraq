package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.ProfileDTO;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.services.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {
    private final ModelMapper modelMapper;
    private final UserServiceImpl userService;

    @Autowired
    public ProfileServiceImpl(ModelMapper modelMapper, UserServiceImpl userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

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
        ProfileDTO profileDTO = modelMapper.map(user, ProfileDTO.class);
        profileDTO.setFullName(user.getFirstName() + " " + user.getLastName());
        return profileDTO;
    }

    private void updateUserData(User user, ProfileDTO profileDTO) {
        String[] fullName = profileDTO.getFullName().split(" ");
        if (fullName.length == 2) {
            user.setFirstName(fullName[0]);
            user.setLastName(fullName[1]);
        }
    }
}
