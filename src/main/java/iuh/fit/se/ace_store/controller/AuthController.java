package iuh.fit.se.ace_store.controller;

import iuh.fit.se.ace_store.dto.request.LoginRequest;
import iuh.fit.se.ace_store.dto.request.RegisterRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.dto.response.AuthResponseDTO;
import iuh.fit.se.ace_store.dto.response.UserResponse;
import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.EmailService;
import iuh.fit.se.ace_store.service.JwtService;
import iuh.fit.se.ace_store.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            UserResponse response = userService.register(request);
            return ResponseEntity.ok(new ApiResponse(true, null, "Đăng ký thành công! Vui lòng kiểm tra email để xác thực.", null, response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "REGISTER_ERROR", e.getMessage(), "Kiểm tra lại thông tin đăng ký.", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
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
        AuthResponseDTO response = new AuthResponseDTO(accessToken, refreshToken, "Bearer");
        return ResponseEntity.ok(new ApiResponse(true, null, "Đăng nhập thành công!", null, response));
    } catch (RuntimeException e) {
        String action = e.getMessage().contains("activated") ? "Vui lòng kiểm tra email để xác thực tài khoản." : null;
        return ResponseEntity.badRequest().body(new ApiResponse(false, "LOGIN_ERROR", e.getMessage(), action, null));
    }
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
    User user = userRepository.findByVerificationToken(token)
        .orElse(null);
    if (user == null) {
        return ResponseEntity.badRequest().body(new ApiResponse(false, "VERIFY_TOKEN_INVALID", "Token không hợp lệ!", "Vui lòng đăng ký lại hoặc kiểm tra email xác thực.", null));
    }
    if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
        return ResponseEntity.badRequest().body(new ApiResponse(false, "VERIFY_TOKEN_EXPIRED", "Token đã hết hạn!", "Vui lòng đăng ký lại để nhận email xác thực mới.", null));
    }
    user.setEnabled(true);
    user.setVerificationToken(null);
    user.setTokenExpiration(null);
    userRepository.save(user);
    String subject = "🎉 Chào mừng bạn đến với PC-Store!";
    String content = """
        <h2>Chào mừng %s!</h2>
        <p>Tài khoản của bạn đã được kích hoạt thành công.</p>
        <p>Giờ bạn có thể đăng nhập và bắt đầu mua sắm ngay!</p>
        """.formatted(user.getFirstName());
    emailService.sendHtmlEmail(user.getEmail(), subject, content);
    return ResponseEntity.ok(new ApiResponse(true, null, "Tài khoản của bạn đã được xác nhận thành công!", null, null));
    }

    @GetMapping("/success")
    public ResponseEntity<ApiResponse> loginSuccess() {
        return ResponseEntity.ok(new ApiResponse(true, null, "Google login successful!", null, null));
    }
}
