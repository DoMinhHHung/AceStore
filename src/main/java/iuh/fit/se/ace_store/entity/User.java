package iuh.fit.se.ace_store.entity;

import iuh.fit.se.ace_store.entity.enums.AuthProvider;
import iuh.fit.se.ace_store.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table( name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = {"email", "provider"}) } )
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phone;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String address;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;
    private String verificationToken;
    private LocalDateTime tokenExpiration;
}

