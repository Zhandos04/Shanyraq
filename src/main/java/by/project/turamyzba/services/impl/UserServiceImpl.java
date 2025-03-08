package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.UserDTO;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.jwt.JwtService;
import by.project.turamyzba.repositories.UserRepository;
import by.project.turamyzba.services.UserService;
import by.project.turamyzba.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmailServiceImpl emailService;
    private final GoogleAuthService googleAuthService;
    private final JwtService jwtService;

    @Transactional
    @Override
    public String authenticateWithGoogleCode(String code) {
        // 1. Обменять code на access_token
        String accessToken = googleAuthService.exchangeCodeForToken(code);
        // 2. Получить userinfo от Google
        GoogleAuthService.GoogleUserInfo userInfo = googleAuthService.getUserInfo(accessToken);

        // 3. Проверить пользователя в БД
        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
        User user;
        if (userOptional.isEmpty()) {
            // Создаём нового пользователя
            user = new User();
            user.setEmail(userInfo.getEmail());
            user.setFirstName("");
            user.setLastName("");
            user.setPassword("");
            user.setGender("Любой");
            user.setIsVerified(true);
            userRepository.save(user);
        } else {
            user = userOptional.get();
        }

        // 4. Генерируем JWT
        Map<String, String> tokens = jwtService.generateTokens(user.getEmail());

        return tokens.get("accessToken");
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Transactional
    @Override
    public void registerNewUser(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("A user with that email already exists");
        }
        User user = convertToUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsVerified(false);
        user.setIsSurveyCompleted(false);
        user.setGender("Любой");
        userRepository.save(user);
        String code = generateCode();
        saveUserConfirmationCode(user.getId(), code);
        try {
            emailService.sendEmail(userDTO.getEmail(), "Shanyraq Verify Email", "Your code is: " + code);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while sending the verification email.");
        }
    }

    @Transactional
    @Override
    public void update(User user){
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateProfile(User user){
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void saveUserConfirmationCode(Long id, String code) {
        User user = userRepository.getUserById(id);
        user.setConfirmationCode(code);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updatePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private String generateCode() {
        return Integer.toString((int)(Math.random() * 900000) + 100000);
    }

    public UserDetails getCurrentUser() {
        // Получаем текущую аутентификацию
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Если пользователь аутентифицирован
        if (authentication != null && authentication.isAuthenticated()) {
            // Возвращаем объект UserDetails (который был установлен ранее в фильтре)
            return (UserDetails) authentication.getPrincipal();
        }

        return null;
    }

    @Override
    @Transactional
    public void resentCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        String code = generateCode();
        saveUserConfirmationCode(user.getId(), code);
        emailService.sendEmail(email, "Shanyraq Resend Code", "Your code is: " + code);
    }
}
