package iuh.fit.se.ace_store.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}