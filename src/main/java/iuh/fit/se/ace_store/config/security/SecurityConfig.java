package iuh.fit.se.ace_store.config.security;

import iuh.fit.se.ace_store.config.oauth2.CustomOAuth2UserService;
import iuh.fit.se.ace_store.config.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2UserService OAuth2UserService;

    public SecurityConfig(OAuth2SuccessHandler oAuth2SuccessHandler, CustomOAuth2UserService OAuth2UserService) {
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.OAuth2UserService = OAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/oauth2/**",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(OAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )
                .formLogin(login -> login.disable())
                .logout(logout -> logout.disable())
                .httpBasic(basic -> basic.disable())
                .rememberMe(me -> me.disable());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
