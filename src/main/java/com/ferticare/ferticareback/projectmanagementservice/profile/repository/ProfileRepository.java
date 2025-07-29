package com.ferticare.ferticareback.projectmanagementservice.profile.repository;

import com.ferticare.ferticareback.projectmanagementservice.profile.entity.Profile;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Department;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<Profile> findByUser_Id(UUID userId);
    Optional<Profile> findByUser(User user);
    
    @Transactional
    void deleteByUser(User user);
    
    // Tìm tất cả profile theo department với phân trang
    @Query("SELECT p FROM Profile p JOIN FETCH p.user WHERE p.department = :department")
    Page<Profile> findByDepartment(@Param("department") Department department, Pageable pageable);
}
