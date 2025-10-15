package iuh.fit.se.ace_store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String gender;
    private LocalDate dob;
}
