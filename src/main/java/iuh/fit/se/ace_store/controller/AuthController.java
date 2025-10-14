package iuh.fit.se.ace_store.controller;

import iuh.fit.se.ace_store.dto.request.LoginRequest;
import iuh.fit.se.ace_store.dto.request.RegisterRequest;
import iuh.fit.se.ace_store.dto.response.UserResponse;
import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.EmailService;
import iuh.fit.se.ace_store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("❌ Token không hợp lệ!");
        }

        if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("⏰ Token đã hết hạn rồi bro!");
        }

        user.setEnabled(true);
        user.setVerificationToken(null);
        user.setTokenExpiration(null);
        userRepository.save(user);

        // Gửi mail cảm ơn sau khi xác nhận
        String subject = "🎉 Chào mừng bạn đến với PC-Store!";
        String content = """
                <h2>Chào mừng %s!</h2>
                <p>Tài khoản của bạn đã được kích hoạt thành công.</p>
                <p>Giờ bạn có thể đăng nhập và bắt đầu mua sắm ngay!</p>
                """.formatted(user.getFirstName());
        emailService.sendHtmlEmail(user.getEmail(), subject, content);

        return ResponseEntity.ok("✅ Tài khoản của bạn đã được xác nhận thành công!");
    }

    @GetMapping("/success")
    public ResponseEntity<String> loginSuccess() {
        return ResponseEntity.ok("Google login successful!");
    }
}
