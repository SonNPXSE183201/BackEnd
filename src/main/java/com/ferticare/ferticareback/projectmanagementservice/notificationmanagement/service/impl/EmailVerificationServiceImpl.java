package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.dto.VerifyResponse;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.repository.EmailVerificationTokenRepository;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailVerificationService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public VerifyResponse verifyEmail(String token) {
        return tokenRepository.findByToken(token)
                .map(verificationToken -> {
                    // Check if token is expired
                    if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                        return new VerifyResponse("Token hết hạn", false);
                    }

                    // Update user verification status
                    User user = verificationToken.getUser();
                    user.setEmailVerified(true);
                    userRepository.save(user);
                    
                    // Cleanup token (optional)
                    tokenRepository.delete(verificationToken);

                    return new VerifyResponse("Email xác thực thành công!", true);
                })
                .orElse(new VerifyResponse("Token không hợp lệ", false));
    }
} 