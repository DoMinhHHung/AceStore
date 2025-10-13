package iuh.fit.se.ace_store.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String address;
    private String role;
    private boolean enabled;
    private String provider;
}
