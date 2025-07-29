package com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.Role;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.Gender;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.RoleType;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.RoleRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.GoogleLoginRequest;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.response.LoginResponse;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final JwtUtil jwtUtil;
    private final UserServiceImpl userServiceImpl;

    @Value("${google.oauth.client-id}")
    private String googleClientId;

    @Override
    @Transactional
    public LoginResponse authenticateGoogleUser(GoogleLoginRequest googleLoginRequest) {
        // TODO: In production, verify Google token here
        // For now, we'll accept the token and proceed with user data
        
        if (!verifyGoogleToken(googleLoginRequest.getGoogleToken())) {
            throw new IllegalArgumentException("Invalid Google token");
        }

        String email = googleLoginRequest.getEmail();
        String fullName = googleLoginRequest.getFullName();
        String avatarUrl = googleLoginRequest.getAvatarUrl();

        // Find existing user or create new one
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        User user;
        if (existingUser.isPresent()) {
            // User already exists, update avatar if needed
            user = existingUser.get();
            if (avatarUrl != null && !avatarUrl.equals(user.getAvatarUrl())) {
                user.setAvatarUrl(avatarUrl);
                user = userRepository.save(user);
            }
        } else {
            // Create new user from Google data
            user = User.builder()
                    .fullName(fullName)
                    .email(email)
                    .avatarUrl(avatarUrl)
                    .gender(Gender.MALE) // Default gender, can be updated later
                    .dateOfBirth(LocalDate.of(1990, 1, 1)) // Default date, can be updated later
                    .isEmailVerified(true) // Google emails are pre-verified
                    .build();
            
            // Use the new saveGoogleUser method
            user = userServiceImpl.saveGoogleUser(user, RoleType.CUSTOMER);
            
            // Create profile for new user
            Profile profile = Profile.builder()
                    .user(user)
                    .status("active")
                    .build();
            profileRepository.save(profile);
            
            log.info("Created new user from Google OAuth: {}", email);
        }

        // Generate JWT token
        Role userRole = roleRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        String token = jwtUtil.generateToken(user.getId(), userRole.getRoleType());

        return LoginResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .token(token)
                .build();
    }

    @Override
    public boolean verifyGoogleToken(String token) {
        // TODO: Implement actual Google token verification
        // For now, we'll do basic validation
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        // In production, you would verify the token with Google's API:
        // https://developers.google.com/identity/sign-in/web/backend-auth
        
        log.info("Verifying Google token (mock implementation)");
        return true; // Mock verification - always returns true for development
    }
} 