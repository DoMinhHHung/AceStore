package iuh.fit.se.ace_store.config.oauth2;

import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            User newUser = User.builder()
                    .email(email)
                    .firstName(name)
                    .lastName("")
                    .provider(User.AuthProvider.GOOGLE)
                    .enabled(true)
                    .role(User.Role.USER)
                    .build();

            userRepository.save(newUser);

            String subject = "🎉 Chào mừng bạn đến với PC-Store!";
            String content = """
                <h2>Xin chào %s!</h2>
                <p>Cảm ơn bạn đã đăng nhập bằng Google tại <b>PC-Store</b>.</p>
                <p>Chúc bạn có trải nghiệm mua sắm tuyệt vời!!!</p>
            """.formatted(name);
            emailService.sendHtmlEmail(email, subject, content);
        }

        response.sendRedirect("/api/auth/success");
    }
}

