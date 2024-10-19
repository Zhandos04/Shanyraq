package by.project.turamyzba.services;

import by.project.turamyzba.models.User;
import by.project.turamyzba.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = getUserByEmail(username).get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Transactional
    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void update(User user){
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }
    @Transactional
    public void saveUserConfirmationCode(Integer id, String code) {
        User user = userRepository.getUserById(id);
        user.setConfirmationCode(code);
        user.setIsVerified(false);
        userRepository.save(user);
    }
    @Transactional
    public void updatePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

//    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
//        return userRepository.findByPhoneNumber(phoneNumber);
//    }
}