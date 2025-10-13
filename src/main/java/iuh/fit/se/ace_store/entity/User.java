package iuh.fit.se.ace_store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private Role role;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;
    // Enum roles
    public enum Role { USER, ADMIN } // Enum AuthProvider
    public enum AuthProvider { LOCAL, GOOGLE } }