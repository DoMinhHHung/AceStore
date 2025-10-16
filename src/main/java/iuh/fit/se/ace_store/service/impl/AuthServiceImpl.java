package iuh.fit.se.ace_store.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.entity.PasswordResetToken;
import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.repository.PasswordResetTokenRepository;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class AuthServiceImpl {
    @Service
    @RequiredArgsConstructor
    public static class EmailServiceImpl implements AuthService.EmailService {

        private final JavaMailSender mailSender;

        @Override
        public void sendHtmlEmail(String to, String subject, String htmlContent) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);
                mailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException("Lỗi gửi email: " + e.getMessage());
            }
        }
    }

    @Service
    @RequiredArgsConstructor
    public static class JwtServiceImpl implements AuthService.JwtService {

        @Value("${jwt.secret}")
        private String secretKey;

        @Value("${jwt.expiration}")
        private long jwtExpiration;

        @Value("${jwt.refresh-token.expiration}")
        private long refreshExpiration;

        private Key getSignInKey() {
            return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String generateToken(UserDetails userDetails) {
            return buildToken(userDetails, jwtExpiration);
        }

        @Override
        public String generateRefreshToken(UserDetails userDetails) {
            return buildToken(userDetails, refreshExpiration);
        }

        private String buildToken(UserDetails userDetails, long expiration) {
            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        @Override
        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        private <T> T extractClaim(String token, Function<Claims, T> resolver) {
            return resolver.apply(Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody());
        }

        @Override
        public boolean isTokenValid(String token, UserDetails userDetails) {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isExpired(token);
        }

        private boolean isExpired(String token) {
            Date exp = Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return exp.before(new Date());
        }
    }

    @Service
    @RequiredArgsConstructor
    public static class PasswordResetServiceImpl implements AuthService.PasswordResetService {
        private final UserRepository userRepository;
        private final PasswordResetTokenRepository tokenRepository;
        private final AuthService.EmailService emailService;
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
}
