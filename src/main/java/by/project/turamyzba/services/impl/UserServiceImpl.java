package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.UserDTO;
import by.project.turamyzba.models.User;
import by.project.turamyzba.repositories.UserRepository;
import by.project.turamyzba.services.EmailService;
import by.project.turamyzba.services.UserService;
import by.project.turamyzba.util.UserAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = getUserByEmail(username).get();
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
        userRepository.save(user);
        String code = generateCode();
        saveUserConfirmationCode(user.getId(), code);
        emailService.sendEmail(userDTO.getEmail(), "Turamyzba Verity Email", "Your code is: " + code);
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
    public void saveUserConfirmationCode(Integer id, String code) {
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
        return Integer.toString((int)(Math.random() * 9000) + 1000);
    }
}
