package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {
    Optional<EmailVerificationToken> findByToken(String token);
}