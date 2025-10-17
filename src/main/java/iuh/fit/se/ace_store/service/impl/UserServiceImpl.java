package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.dto.request.AuthRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.dto.response.UserResponse;
import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.entity.enums.AuthProvider;
import iuh.fit.se.ace_store.entity.enums.Role;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.AuthService;
import iuh.fit.se.ace_store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${ace.verify-link}")
    private String verifyLinkProperty;

    private final UserRepository userRepository;
    private final AuthService.EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserResponse register(AuthRequest.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Email or Phone already exists");
        }
        User user = User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .gender(request.getGender())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .provider(AuthProvider.LOCAL)
                .build();

        // verification token (expires in 1 hour)
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusHours(1));

        userRepository.save(user);

        String verifyLink = verifyLinkProperty + token;

        String html = """
            <h3>Hello %s,</h3>
            <p>Thank you for registering at <b>Ace-Store</b>!</p>
            <p>Click the link below to verify your account (expires in 1 hour):</p>
            <a href="%s">Verify account</a>
            """.formatted(user.getFirstName(), verifyLink);

        emailService.sendHtmlEmail(user.getEmail(), "Account verification - Ace Store", html);

        return toResponse(user);
    }

    @Override
    public UserResponse login(AuthRequest.LoginRequest request) {
        User user = userRepository.findByEmail(request.getUsername())
                .or(() -> userRepository.findByPhone(request.getUsername()))
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not activated yet. Check your email.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return toResponse(user);
    }

    @Override
    public ApiResponse updateUserProfile(String email, AuthRequest.UpdateUserRequest dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setDob(dto.getDob());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());

        userRepository.save(user);
        return ApiResponse.success("Profile updated successfully", user);
    }

    @Override
    public ApiResponse changePassword(String email, AuthRequest.ChangePasswordRequest dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return ApiResponse.error("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return ApiResponse.success("Password changed successfully");
    }

    @Override
    public ApiResponse updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        try {
            Role newRole = Role.valueOf(roleName.toUpperCase());
            user.setRole(newRole);
            userRepository.save(user);
            return ApiResponse.success("Role updated successfully", user);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error("Invalid role");
        }
    }

    @Override
    public ApiResponse getCurrentUser() {
        User user = getAuthenticatedUser();
        return ApiResponse.success("User info retrieved successfully", toResponse(user));
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dob(user.getDob())
                .gender(user.getGender())
                .address(user.getAddress())
                .role(user.getRole().name())
                .enabled(user.isEnabled())
                .provider(user.getProvider().name())
                .build();
    }
}