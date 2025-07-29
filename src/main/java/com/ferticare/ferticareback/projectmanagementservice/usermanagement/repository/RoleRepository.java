package com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.Role;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByUserId(UUID userId);
    Optional<Role> findByUser(User user);
    
    @Transactional
    void deleteByUser(User user);
}
