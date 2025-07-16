package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.entity.PasswordResetToken;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.repository.PasswordResetTokenRepository;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailService;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.PasswordResetService;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;
import static com.ferticare.ferticareback.common.constant.DataPatternConstant.PASSWORD_PATTERN;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email không tồn tại!"));
        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Email chưa xác thực!");
        }
        // Xóa token cũ nếu có
        passwordResetTokenRepository.findAll().stream()
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .forEach(passwordResetTokenRepository::delete);
        // Tạo token mới
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();
        passwordResetTokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(user, token);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token không hợp lệ!"));
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token đã hết hạn!");
        }
        User user = resetToken.getUser();
        // Chặn user Google
        if (user.getPassword() == null || "GOOGLE_AUTH".equals(user.getPassword())) {
            throw new IllegalArgumentException("Tài khoản Google không thể đặt lại mật khẩu qua chức năng này!");
        }
        // Validate định dạng mật khẩu
        if (!Pattern.matches(PASSWORD_PATTERN, newPassword)) {
            throw new IllegalArgumentException("Mật khẩu phải tối thiểu 8 ký tự, có chữ hoa, chữ thường và số!");
        }
        
        // *** BỔ SUNG ĐOẠN NÀY ***
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }
}