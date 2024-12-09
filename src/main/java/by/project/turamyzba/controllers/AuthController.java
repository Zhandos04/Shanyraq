package by.project.turamyzba.controllers;

import by.project.turamyzba.config.CustomAuthenticationProvider;
import by.project.turamyzba.dto.requests.*;
import by.project.turamyzba.dto.responses.AuthDTO;
import by.project.turamyzba.jwt.JwtService;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.services.EmailService;
import by.project.turamyzba.services.TokenBlacklistService;
import by.project.turamyzba.services.UserService;
import by.project.turamyzba.exceptions.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Tag(name="Auth", description="Взаймодействие с пользователями")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final CustomAuthenticationProvider authenticationProvider;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final TokenBlacklistService tokenBlacklistService;


    @PostMapping("/signup")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user by checking for existing IDs and phone numbers.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Code sent successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input, validation failed"),
                    @ApiResponse(responseCode = "406", description = "User already exists"),
                    @ApiResponse(responseCode = "500", description = "Unexpected error occurred (internal server error)"),
                    @ApiResponse(responseCode = "500", description = "Error occurred while sending the verification email")
            }
    )
    public ResponseEntity<String> register(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) throws UserAlreadyExistsException {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        userService.registerNewUser(userDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Код успешно отправлен!");
    }

    @PostMapping("/verify-email")
    @Operation(
            summary = "Verify Email",
            description = "Verifies the reset code entered by the user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Incorrect reset code"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> verifyEmail(@RequestBody CodeDTO codeDTO) {
        Optional<User> userOptional = userService.getUserByEmail(codeDTO.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        User user = userOptional.get();
        if (!user.getConfirmationCode().equals(codeDTO.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неправильный пароль");
        }

        user.setIsVerified(true);
        userService.update(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно зарегистрирован!");
    }


    @PostMapping("/resendCode")
    @Operation(
            summary = "Resend code",
            description = "Resends the verification code to the user's email.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Code resent successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found with the provided email"),
                    @ApiResponse(responseCode = "500", description = "Internal server error occurred")
            }
    )
    public ResponseEntity<String> resendCode(@RequestBody ResentCodeDTO resentCodeDTO) {
        userService.resentCode(resentCodeDTO.getEmail());
        return ResponseEntity.ok("Код успешно переотправлен!");
    }

    @PostMapping("/google")
    @Operation(summary = "Login or register with google")
    public ResponseEntity<AuthDTO> googleLogin(@RequestBody AuthRequest request) {
        String token = userService.authenticateWithGoogleCode(request.getCode());
        AuthDTO authDTO = new AuthDTO();
        authDTO.setAccessToken(token);
        return ResponseEntity.ok(authDTO);
    }

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns an Auth token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful, access token returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request, invalid data or email format"),
                    @ApiResponse(responseCode = "401", description = "Incorrect email or password"),
                    @ApiResponse(responseCode = "403", description = "User is not verified yet"),
                    @ApiResponse(responseCode = "500", description = "Internal server error occurred")
            }
    )
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<User> userOptional = userService.getUserByEmail(loginDTO.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильная почта или пароль!");
        }
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userOptional.get().getEmail(), loginDTO.getPassword()));
        if (!userOptional.get().getIsVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Это пользователь еще не верифицирован!");
        }
        Map<String, String> tokens = jwtService.generateTokens(loginDTO.getEmail());

        AuthDTO authDTO = modelMapper.map(userOptional.get(), AuthDTO.class);
        authDTO.setAccessToken(tokens.get("accessToken"));
        return ResponseEntity.ok(authDTO);
    }
    @PostMapping("/logout")
    @Operation(
            summary = "User logout",
            description = "Logs out the user by invalidating the current token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logged out successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid token"),
                    @ApiResponse(responseCode = "500", description = "Internal server error occurred")
            }
    )
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Извлекаем дату истечения токена
            Date expirationTime = jwtService.extractExpiration(token);

            // Добавляем токен в черный список
            tokenBlacklistService.addTokenToBlacklist(token, expirationTime);

            return ResponseEntity.ok("Выход из системы успешно завершен!");
        }
        return ResponseEntity.badRequest().body("Невалидный токен!");
    }

//    @PostMapping("/refresh-token")
//    @Operation(summary = "Refresh Access Token", description = "Refreshes the access token using a valid refresh token.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Access token refreshed successfully"),
//                    @ApiResponse(responseCode = "403", description = "Invalid or expired refresh token"),
//                    @ApiResponse(responseCode = "401", description = "Invalid refresh token")
//            },
//            security = @SecurityRequirement(name = "bearerToken"))
//
//    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
//        String headerAuth = request.getHeader("Authorization");
//        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
//            String refreshToken = headerAuth.substring(7);
//            try {
//                if (jwtService.validateRefreshToken(refreshToken)) { // Проверка валидности рефреш токена
//                    String userName = jwtService.extractUsername(refreshToken);
//                    // Проверка, что пользователь существует и активен
//                    UserDetails userDetails = userService.loadUserByUsername(userName);
//                    if (userDetails != null && !jwtService.isTokenExpired(refreshToken)) {
//                        String newAccessToken = jwtService.generateTokens(userName).get("accessToken");
//                        Map<String, String> tokens = new HashMap<>();
//                        tokens.put("accessToken", newAccessToken);
//                        tokens.put("refreshToken", refreshToken); // Отправляем тот же рефреш токен обратно
//                        return ResponseEntity.ok(tokens);
//                    }
//                }
//            } catch (Exception e) {
//                throw new BadCredentialsException("Invalid refresh token");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired refresh token");
//    }

    @PostMapping("/forgot-password")
    @Operation(
            summary = "Password recovery",
            description = "Initiates a password recovery process by sending a reset code to the user's email.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reset code sent successfully"),
                    @ApiResponse(responseCode = "404", description = "Email not found"),
                    @ApiResponse(responseCode = "403", description = "This user is not verified yet")
            }
    )
    public ResponseEntity<?> forgotPassword(@RequestBody EmailDTO emailDTO) {
        Optional<User> user = userService.getUserByEmail(emailDTO.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Почта не найден!");
        }
        if(!user.get().getIsVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Это пользователь еще не верифицирован!");
        }

        String code = generateCode();
        userService.saveUserConfirmationCode(user.get().getId(), code);

        emailService.sendEmail(emailDTO.getEmail(), "Shanyraq Reset Password", "Your code is: " + code);

        return ResponseEntity.ok("Инструкции по восстановлению пароля были отправлены на вашу электронную почту.");
    }
    @PostMapping("/verify-code")
    @Operation(summary = "Verify reset code", description = "Verifies the reset code entered by the user.")
    @ApiResponse(responseCode = "200", description = "Code is verified!")
    @ApiResponse(responseCode = "400", description = "Incorrect code")
    public ResponseEntity<?> verifyPassword(@RequestBody CodeDTO codeDTO) {
        Optional<User> user = userService.getUserByEmail(codeDTO.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Почта не найден!");
        }
        if(!user.get().getConfirmationCode().equals(codeDTO.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неправильный код!");
        }
        return ResponseEntity.ok("Код верифицирован!");
    }

    @PostMapping("/update-password")
    @Operation(
            summary = "Update user password",
            description = "Updates the user's password after verification.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input (Validation errors)"),
                    @ApiResponse(responseCode = "404", description = "Email not found")
            }
    )
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordDTO updatePasswordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        Optional<User> user = userService.getUserByEmail(updatePasswordDTO.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Почта не найден!");
        }
        user.get().setPassword(updatePasswordDTO.getPassword());
        userService.updatePassword(user.get());
        return ResponseEntity.ok("Пароль обновлен!");
    }

    private String generateCode() {
        return Integer.toString((int)(Math.random() * 900000) + 100000);
    }
}