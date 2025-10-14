package iuh.fit.se.ace_store.config.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.EmailService;
import iuh.fit.se.ace_store.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null) {
            throw new RuntimeException("Không thể lấy email từ tài khoản Google!");
        }

        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isEmpty()) {
            user = User.builder()
                    .email(email)
                    .firstName(name)
                    .lastName("")
                    .provider(User.AuthProvider.GOOGLE)
                    .enabled(true)
                    .role(User.Role.USER)
                    .build();

            userRepository.save(user);

            String subject = "🎉 Chào mừng bạn đến với PC-Store!";
            String content = """
                <h2>Xin chào %s!</h2>
                <p>Cảm ơn bạn đã đăng nhập bằng Google tại <b>PC-Store</b>.</p>
                <p>Chúc bạn có trải nghiệm mua sắm tuyệt vời!!!</p>
            """.formatted(name);
            emailService.sendHtmlEmail(email, subject, content);
        } else {
            user = existingUser.get();
        }

        // Tạo token JWT
        UserDetails principal = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password("")
                .authorities("ROLE_USER")
                .build();

        String accessToken = jwtService.generateToken(principal);
        String refreshToken = jwtService.generateRefreshToken(principal);

        // Trả JSON
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("tokenType", "Bearer");
        tokens.put("message", "Đăng nhập Google thành công!");

        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), tokens);
    }
}
