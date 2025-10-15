package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.entity.PasswordResetToken;
import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.repository.PasswordResetTokenRepository;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.EmailService;
import iuh.fit.se.ace_store.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Value("${ace.reset-password-link}")
    private String resetPasswordLink;

    @Override
    public ApiResponse forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty()){
            return ApiResponse.error("Do not exist this email");
        }
        User user = userOpt.get();
        tokenRepository.deleteByUser(user);
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenRepository.save(resetToken);
    String resetLink = resetPasswordLink + token;
        emailService.sendHtmlEmail(
                user.getEmail(),
                "Reset mật khẩu Ace-Store",
                "Click vào link để reset mật khẩu: " + resetLink
        );

        return ApiResponse.success("Email reset password đã được gửi!");
    }

    @Override
    public ApiResponse resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            return ApiResponse.error("Token không hợp lệ!");
        }

        PasswordResetToken resetToken = tokenOpt.get();
        if (resetToken.isExpired()) {
            return ApiResponse.error("Token đã hết hạn!");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); // Token dùng 1 lần

        return ApiResponse.success("Đặt lại mật khẩu thành công!");
    }
}
