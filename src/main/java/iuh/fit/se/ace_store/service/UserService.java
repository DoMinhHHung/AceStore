package iuh.fit.se.ace_store.service;

import iuh.fit.se.ace_store.dto.request.*;
import iuh.fit.se.ace_store.dto.response.ApiResponse;
import iuh.fit.se.ace_store.dto.response.UserResponse;

public interface UserService {
    UserResponse register(AuthRequest.RegisterRequest request);
    UserResponse login(AuthRequest.LoginRequest request);
    ApiResponse updateUserProfile(String email, AuthRequest.UpdateUserRequest dto);
    ApiResponse changePassword(String email, AuthRequest.ChangePasswordRequest dto);
    ApiResponse updateUserRole(Long userId, String roleName);
    ApiResponse getCurrentUser();

}
