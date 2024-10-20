package by.project.turamyzba.services;

import by.project.turamyzba.dto.ProfileDTO;
import by.project.turamyzba.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProfileService {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public ProfileService(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }
    public ProfileDTO getUser() {
        String email = getEmail();
        User user = userService.getUserByEmail(email).get();
        ProfileDTO profileDTO = convertToProfileDTO(user);
        profileDTO.setFullName(user.getFirstName() + " " + user.getLastName());
        return profileDTO;
    }
    @Transactional
    public void editProfile(ProfileDTO profileDTO) {
        String email = getEmail();
        User updatedUser = userService.getUserByEmail(email).get();
        String[] arr = profileDTO.getFullName().split(" ");
        updatedUser.setFirstName(arr[0]);
        updatedUser.setLastName(arr[1]);
        updatedUser.setNickName(profileDTO.getNickName());
        userService.updateProfile(updatedUser);
    }
    private String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        return username;
    }
    public ProfileDTO convertToProfileDTO(User user) {
        return modelMapper.map(user, ProfileDTO.class);
    }
}
