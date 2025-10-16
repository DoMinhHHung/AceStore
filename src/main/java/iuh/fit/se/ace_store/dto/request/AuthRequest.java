package iuh.fit.se.ace_store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class AuthRequest {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String email;
        private String phone;
        private String firstName;
        private String lastName;
        private LocalDate dob;
        private String gender;
        private String address;
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateUserRequest {
        private String firstName;
        private String lastName;
        private String phone;
        private String address;
        private String gender;
        private LocalDate dob;
    }
}
