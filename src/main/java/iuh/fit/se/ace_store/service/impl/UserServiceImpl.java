package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.dto.ChangePasswordDTO;
import iuh.fit.se.ace_store.dto.UserProfileDTO;
import iuh.fit.se.ace_store.dto.request.LoginRequest;
import iuh.fit.se.ace_store.dto.request.RegisterRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.dto.response.UserResponse;
import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.entity.enums.AuthProvider;
import iuh.fit.se.ace_store.entity.enums.Role;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.EmailService;
import iuh.fit.se.ace_store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Email or Phone is already exists!!!");
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

        // token xác minh (hết hạn sau 1 tiếng)
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusHours(1));

        userRepository.save(user);

        String verifyLink = "http://localhost:8080/api/auth/verify?token=" + token;

        String html = """
            <h3>Xin chào %s,</h3>
            <p>Cảm ơn bạn đã đăng ký tại <b>PC-Store</b>!</p>
            <p>Nhấn vào link bên dưới để xác nhận tài khoản (hết hạn sau 1 tiếng):</p>
            <a href="%s">Xác nhận tài khoản</a>
            """.formatted(user.getFirstName(), verifyLink);

        emailService.sendHtmlEmail(user.getEmail(), "Xác nhận tài khoản - PC Store", html);

        return toResponse(user);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getUsername())
                .or(() -> userRepository.findByPhone(request.getUsername()))
                .orElseThrow(() -> new RuntimeException("Account not exists!!!"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not activated yet! Check your email.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password!!!");
        }

        return toResponse(user);
    }

    @Override
    public ApiResponse updateUserProfile(String email, UserProfileDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setDob(dto.getDob());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());

        userRepository.save(user);
        return ApiResponse.success("Cập nhật thông tin thành công!", user);
    }

    @Override
    public ApiResponse changePassword(String email, ChangePasswordDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return ApiResponse.error("Mật khẩu cũ không đúng!");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return ApiResponse.success("Đổi mật khẩu thành công!");
    }

    @Override
    public ApiResponse updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user ID: " + userId));

        try {
            Role newRole = Role.valueOf(roleName.toUpperCase());
            user.setRole(newRole);
            userRepository.save(user);
            return ApiResponse.success("Cập nhật quyền thành công!", user);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error("Role không hợp lệ!");
        }
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