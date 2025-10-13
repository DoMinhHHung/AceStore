package iuh.fit.se.ace_store.service.impl;

import iuh.fit.se.ace_store.dto.request.LoginRequest;
import iuh.fit.se.ace_store.dto.request.RegisterRequest;
import iuh.fit.se.ace_store.dto.response.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);
    UserResponse login(LoginRequest request);
}
