package com.ferticare.ferticareback.projectmanagementservice.profile.repository;

import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
//    Optional<Profile> findByUser_Id(UUID id);
    Optional<Profile> findByUser_Id(UUID userId);
}