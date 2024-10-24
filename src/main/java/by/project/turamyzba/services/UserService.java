package by.project.turamyzba.services;

import by.project.turamyzba.dto.UserDTO;
import by.project.turamyzba.models.User;
import by.project.turamyzba.util.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;

public interface UserService {
    UserDetails loadUserByUsername(String username);
    void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException;
    void update(User user);
    void updateProfile(User user);
    void saveUserConfirmationCode(Long id, String code);
    void updatePassword(User user);
    boolean isNickNameTaken(String nickName);
    Optional<User> getUserByEmail(String email);
    UserDetails getCurrentUser();
}
