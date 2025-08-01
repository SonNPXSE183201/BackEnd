package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);
} 