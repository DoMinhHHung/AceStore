package iuh.fit.se.ace_store.config.security;

import iuh.fit.se.ace_store.config.oauth2.OAuth2SuccessHandler;
import iuh.fit.se.ace_store.config.security.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthentication jwtAuthenticationFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
            String json = """
                {
                    "success": false,
                    "errorCode": "UNAUTHORIZED",
                    "message": "Bạn chưa đăng nhập hoặc token không hợp lệ!",
                    "action": "Vui lòng đăng nhập lại.",
                    "data": null
                }
            """;
            response.getWriter().write(json);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/ace/auth/**",          // login, register, forgot/reset password
                "/oauth2/**",            // google oauth2
                "/login", "/error",
                "/auth/success",
                "/swagger-ui/**", "/v3/api-docs/**"
            ).permitAll()

            // Admin endpoints
            .requestMatchers("/ace/admin/**").hasRole("ADMIN")

            //  User endpoints
            .requestMatchers("/ace/user/**").hasAnyRole("USER", "ADMIN")

            //  Public products (ai cũng xem được)
            .requestMatchers("/ace/products/**").permitAll()

            // Còn lại phải có token mới vào
            .anyRequest().authenticated()
        )
        .oauth2Login(oauth -> oauth
            .successHandler(oAuth2SuccessHandler)
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()));

    return http.build();
    }

    /**
     * Cho phép inject AuthenticationManager để xác thực thủ công (trong login service)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
