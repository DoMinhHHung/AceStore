package iuh.fit.se.ace_store.service;

import iuh.fit.se.ace_store.dto.ChangePasswordDTO;
import iuh.fit.se.ace_store.dto.UserProfileDTO;
import iuh.fit.se.ace_store.dto.request.LoginRequest;
import iuh.fit.se.ace_store.dto.request.RegisterRequest;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.dto.response.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);
    UserResponse login(LoginRequest request);
    ApiResponse updateUserProfile(String email, UserProfileDTO dto);
    ApiResponse changePassword(String email, ChangePasswordDTO dto);
    ApiResponse updateUserRole(Long userId, String roleName);

}
