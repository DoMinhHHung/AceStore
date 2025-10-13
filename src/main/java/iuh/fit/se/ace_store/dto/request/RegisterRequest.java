package iuh.fit.se.ace_store.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String address;
    private String password;
}
