package iuh.fit.se.ace_store.config.oauth2;

import iuh.fit.se.ace_store.entity.User;
import iuh.fit.se.ace_store.repository.UserRepository;
import iuh.fit.se.ace_store.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            User newUser = User.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .enabled(true)
                    .provider(User.AuthProvider.GOOGLE)
                    .role(User.Role.USER)
                    .build();
            userRepository.save(newUser);

            String subject = "🎉 Chào mừng bạn đến với PC-Store!";
            String content = """
                    <h2>Xin chào %s,</h2>
                    <p>Chúc mừng bạn đã đăng nhập bằng Google thành công tại <b>PC-Store</b>!</p>
                    <p>Chúng tôi rất vui khi được đồng hành cùng bạn 💖</p>
                    """.formatted(firstName);
            emailService.sendHtmlEmail(email, subject, content);
        }

        return oAuth2User;
    }
}
