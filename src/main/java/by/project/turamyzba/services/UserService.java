package by.project.turamyzba.services;

import by.project.turamyzba.dto.requests.UserDTO;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.exceptions.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;

public interface UserService {
    UserDetails loadUserByUsername(String username);
    void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException;
    void update(User user);
    void updateProfile(User user);
    void saveUserConfirmationCode(Long id, String code);
    void updatePassword(User user);
    Optional<User> getUserByEmail(String email);
    UserDetails getCurrentUser();
    void resentCode(String email);
}
