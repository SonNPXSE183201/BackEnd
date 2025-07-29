package com.ferticare.ferticareback.projectmanagementservice.configuration.security;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Mã hóa mật khẩu với BCrypt
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
                .cors(withDefaults()) // ✅ enable CORS with custom config
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/google-login",
                                "/api/users",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/api/services",
                                "/api/notifications/verify-email",
                                "/api/notifications/request-password-reset",
                                "/api/notifications/reset-password",
                                "/api/blogs/published",
                                "/api/blog-images/**",
                                "/api/blogs/{id}",
                                "/api/blogs/{id}/view-count",
                                "/api/comments/blog/{blogId}",
                                "/api/comments/blog/{blogId}/count"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/service-request").authenticated()
                        .requestMatchers("/api/service-request/available-doctors/**").authenticated()
                        .requestMatchers("/api/service-request/doctor-available-times/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/comments").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/comments/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/*").authenticated()
                        .requestMatchers("/api/doctor/schedule").hasAuthority("DOCTOR")
                        .requestMatchers("/api/doctor/schedule/**").hasAuthority("DOCTOR")
                        .requestMatchers("/api/treatment-plans/**").hasAuthority("DOCTOR")
                        .requestMatchers("/api/treatment-results/**").hasAuthority("DOCTOR")
                        .requestMatchers("/api/treatment-statistics/**").hasAuthority("DOCTOR")
                        .requestMatchers("/api/doctors").hasAuthority("MANAGER")
                        .requestMatchers("/api/blogs/all").hasAuthority("MANAGER")
                        .requestMatchers("/api/blogs/status/{status}").hasAuthority("MANAGER")
                        .requestMatchers("/api/blogs/pending").hasAuthority("MANAGER")
                        .requestMatchers("/api/blogs/approve/{id}").hasAuthority("MANAGER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(withDefaults()); // ✅ CHỈ CẦN GỌI cors(withDefaults()) đúng cách

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Địa chỉ Front-end
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}