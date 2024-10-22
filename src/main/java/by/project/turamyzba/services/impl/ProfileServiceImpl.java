package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.ProfileDTO;
import by.project.turamyzba.models.User;
import by.project.turamyzba.services.ProfileService;
import by.project.turamyzba.services.UserService;
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
    private final UserService userService;

    @Autowired
    public ProfileServiceImpl(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public ProfileDTO getUser() {
        String email = getEmail();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return convertToProfileDTO(user);
    }

    @Override
    @Transactional
    public void editProfile(ProfileDTO profileDTO) {
        String email = getEmail();
        User updatedUser = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        updateUserData(updatedUser, profileDTO);
        userService.updateProfile(updatedUser);
    }

    private String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
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
        user.setNickName(profileDTO.getNickName());
    }
}
