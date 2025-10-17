package iuh.fit.se.ace_store.controller;

import iuh.fit.se.ace_store.dto.request.*;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.dto.response.AuthResponse;
import iuh.fit.se.ace_store.dto.response.UserResponse;
import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/ace")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthService.EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final AuthService.JwtService jwtService;
    private final AuthService.PasswordResetService passwordResetService;

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse> register(@RequestBody AuthRequest.RegisterRequest request) {
        try {
            UserResponse response = userService.register(request);
            return ResponseEntity.ok(new ApiResponse(true, null, "Registration successful. Please check your email to verify your account.", null, response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "REGISTER_ERROR", e.getMessage(), "Check registration data.", null));
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequest.LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);
            AuthResponse response = new AuthResponse(accessToken, refreshToken, "Bearer");
            return ResponseEntity.ok(new ApiResponse(true, null, "Login successful", null, response));
        } catch (RuntimeException e) {
            String action = e.getMessage().contains("activated") ? "Please check your email to verify your account." : null;
            return ResponseEntity.badRequest().body(new ApiResponse(false, "LOGIN_ERROR", e.getMessage(), action, null));
        }
    }

    @GetMapping("/auth/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "VERIFY_TOKEN_INVALID", "Invalid token", "Please re-register or check your verification email.", null));
        }
        if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "VERIFY_TOKEN_EXPIRED", "Token expired", "Please re-register to receive a new verification email.", null));
        }
        user.setEnabled(true);
        user.setVerificationToken(null);
        user.setTokenExpiration(null);
        userRepository.save(user);
        String subject = "Welcome to Ace-Store!";
        String content = """
            <h2>Welcome %s!</h2>
            <p>Your account has been successfully activated.</p>
            <p>You can now log in and start shopping.</p>
            """.formatted(user.getFirstName());
        emailService.sendHtmlEmail(user.getEmail(), subject, content);
        return ResponseEntity.ok(new ApiResponse(true, null, "Your account has been verified successfully", null, null));
    }

    @GetMapping("/success")
    public ResponseEntity<ApiResponse> loginSuccess() {
        return ResponseEntity.ok(new ApiResponse(true, null, "Google login successful!", null, null));
    }

    @PutMapping("/user/profile")
    public ResponseEntity<ApiResponse> updateUserProfile(@RequestBody AuthRequest.UpdateUserRequest dto, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        ApiResponse response = userService.updateUserProfile(email, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody AuthRequest.ChangePasswordRequest dto, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        ApiResponse response = userService.changePassword(email, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/me")
    public ResponseEntity<ApiResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(passwordResetService.forgotPassword(email));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        return ResponseEntity.ok(passwordResetService.resetPassword(token, newPassword));
    }
}
