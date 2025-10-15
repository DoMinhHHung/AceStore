package iuh.fit.se.ace_store.service;

import iuh.fit.se.ace_store.dto.response.ApiResponse;

public interface PasswordResetService {
    ApiResponse forgotPassword(String email);
    ApiResponse resetPassword(String token, String newPassword);
}
