package iuh.fit.se.ace_store.service;

import iuh.fit.se.ace_store.dto.response.ApiResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    interface JwtService {
        String generateToken(UserDetails userDetails);
        String generateRefreshToken(UserDetails userDetails);
        boolean isTokenValid(String token, UserDetails userDetails);
        String extractUsername(String token);
    }

    interface EmailService {
        void sendHtmlEmail(String to, String subject, String htmlBody);
    }

    interface PasswordResetService {
        ApiResponse forgotPassword(String email);
        ApiResponse resetPassword(String token, String newPassword);
    }
}
