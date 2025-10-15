package iuh.fit.se.ace_store.config.admin;

import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.entity.enums.AuthProvider;
import iuh.fit.se.ace_store.entity.enums.Role;
import iuh.fit.se.ace_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@ace-store.com";
        String adminPhone = "0909000000";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = User.builder()
                    .email(adminEmail)
                    .phone(adminPhone)
                    .firstName("Admin")
                    .lastName("AceStore")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .provider(AuthProvider.LOCAL)
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Admin account created: " + adminEmail + " / 123456");
        } else {
            System.out.println("⚡ Admin already exists: " + adminEmail);
        }
    }
}
