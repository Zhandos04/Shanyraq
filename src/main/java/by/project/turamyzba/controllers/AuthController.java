package by.project.turamyzba.controllers;

import by.project.turamyzba.config.CustomAuthenticationProvider;
import by.project.turamyzba.dto.*;
import by.project.turamyzba.jwt.JwtService;
import by.project.turamyzba.models.User;
import by.project.turamyzba.services.EmailService;
import by.project.turamyzba.services.UserService;
import by.project.turamyzba.util.IncorrectJSONException;
import by.project.turamyzba.util.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name="Auth", description="Взаймодействие с пользователями")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final CustomAuthenticationProvider authenticationProvider;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, JwtService jwtService, CustomAuthenticationProvider authenticationProvider, ModelMapper modelMapper, EmailService emailService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationProvider = authenticationProvider;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @PostMapping( "/signup")
    @Operation(summary = "Register a new user", description = "Registers a new user by checking for existing IDs and phone numbers.")
    @ApiResponse(responseCode = "202", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data provided", content = @Content)
    public HttpStatus register(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) throws UserAlreadyExistsException, IncorrectJSONException {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder str = new StringBuilder();
            for (FieldError error : errors) {
                str.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(";\n");
            }
            throw new IncorrectJSONException(str.toString());
        }
        Optional<User> userOptional = userService.getUserByEmail(userDTO.getEmail());
        if (userOptional.isPresent()){
            throw new UserAlreadyExistsException("a user with that id already exists");
        }
//        Optional<User> userOptional1 = userService.getUserByPhoneNumber(userDTO.getPhoneNumber());
//        if (userOptional1.isPresent()){
//            throw new UserAlreadyExistsException("a user with that phone number already exists");
//        }
        userService.save(convertToUser(userDTO));

        Optional<User> user = userService.getUserByEmail(userDTO.getEmail());

        String code = generateCode();
        userService.saveUserConfirmationCode(user.get().getId(), code);

        emailService.sendEmail(userDTO.getEmail(), "Turamyzba Verity Email", "Your code is: " + code);
        return HttpStatus.OK;
    }

    @PostMapping("/verify-email")
    @Operation(summary = "Verify Email", description = "Verifies the reset code entered by the user.")
    @ApiResponse(responseCode = "200", description = "Reset code verified successfully")
    @ApiResponse(responseCode = "401", description = "Incorrect reset code")
    public HttpStatus verifyEmail(@RequestBody CodeDTO codeDTO) {
        Optional<User> user = userService.getUserByEmail(codeDTO.getEmail());
        if(!user.get().getConfirmationCode().equals(codeDTO.getCode())) {
            throw new BadCredentialsException("Incorrect Code");
        }
        user.get().setIsVerified(true);

        userService.update(user.get());
        return HttpStatus.OK;
    }


    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns an Auth token.")
    @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(schema = @Schema(implementation = AuthDTO.class)))
    @ApiResponse(responseCode = "401", description = "Incorrect ID or password")
    public ResponseEntity<AuthDTO> login(@RequestBody LoginDTO loginDTO) {
        Optional<User> userOptional = userService.getUserByEmail(loginDTO.getEmail());
        if (userOptional.isEmpty()){
            throw new UsernameNotFoundException("Incorrect ID or password");
        } else if (!userOptional.get().getIsVerified()) {
            throw new BadCredentialsException("This user is not verified yet");
        }
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userOptional.get().getEmail(), loginDTO.getPassword()));
        AuthDTO authDTO = modelMapper.map(userOptional.get(), AuthDTO.class);
        authDTO.setToken(jwtService.generateToken(authDTO.getEmail()));
        return new ResponseEntity<>(authDTO, HttpStatus.OK);
    }
    @PostMapping("/forgot-password")
    @Operation(summary = "Password recovery", description = "Initiates a password recovery process by sending a reset code to the user's email.")
    @ApiResponse(responseCode = "200", description = "Reset code sent successfully")
    @ApiResponse(responseCode = "404", description = "Email not found")
    public HttpStatus forgotPassword(@RequestBody EmailDTO emailDTO) {
        Optional<User> user = userService.getUserByEmail(emailDTO.getEmail());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Email not found");
        }

        String code = generateCode();
        userService.saveUserConfirmationCode(user.get().getId(), code);

        emailService.sendEmail(emailDTO.getEmail(), "Turamyzba Reset Password", "Your code is: " + code);

        return HttpStatus.OK;
    }
    @PostMapping("/verify-password")
    @Operation(summary = "Verify reset code", description = "Verifies the reset code entered by the user.")
    @ApiResponse(responseCode = "200", description = "Reset code verified successfully")
    @ApiResponse(responseCode = "401", description = "Incorrect reset code")
    public HttpStatus verifyPassword(@RequestBody CodeDTO codeDTO) {
        Optional<User> admin = userService.getUserByEmail(codeDTO.getEmail());
        if(!admin.get().getConfirmationCode().equals(codeDTO.getCode())) {
            throw new BadCredentialsException("Incorrect Code");
        }
        return HttpStatus.OK;
    }

    @PostMapping("/update-password")
    @Operation(summary = "Update user password", description = "Updates the user's password after verification.")
    @ApiResponse(responseCode = "200", description = "Password updated successfully")
    public HttpStatus updatePassword(@RequestBody LoginDTO loginDTO) {
        Optional<User> user = userService.getUserByEmail(loginDTO.getEmail());
        user.get().setPassword(loginDTO.getPassword());
        userService.updatePassword(user.get());
        return HttpStatus.OK;
    }

    private String generateCode() {
        return Integer.toString((int)(Math.random() * 9000) + 1000);
    }

    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

}