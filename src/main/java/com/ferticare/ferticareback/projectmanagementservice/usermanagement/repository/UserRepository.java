package com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    @Query("""
    SELECT u FROM User u
    JOIN Role r ON r.user = u
    WHERE r.roleType = 'DOCTOR'
""")
    List<User> findAllDoctors();

    @Query("""
    SELECT u.id, u.fullName, p.workSchedule
    FROM User u
    JOIN Role r ON r.user = u
    JOIN Profile p ON p.user = u
    WHERE r.roleType = 'DOCTOR' AND p.status = 'active'
""")
    List<Object[]> findDoctorWithSchedule();

    @Query("""
    SELECT u FROM User u
    JOIN Role r ON u = r.user
    JOIN Profile p ON p.user = u
    WHERE r.roleType = 'DOCTOR'
      AND p.status = 'active'
      AND p.specialty = :specialty
      AND u.id NOT IN :busyDoctorIds
""")
    List<User> findFirstAvailableDoctorExcluding(
            @Param("busyDoctorIds") List<UUID> busyDoctorIds,
            @Param("specialty") String specialty,
            org.springframework.data.domain.Pageable pageable
    );

    @Query("""
    SELECT u.id, u.fullName, p.workSchedule
    FROM User u
    JOIN Role r ON r.user = u
    JOIN Profile p ON p.user = u
    WHERE r.roleType = 'DOCTOR'
      AND p.status = 'active'
      AND TRIM(LOWER(p.specialty)) = TRIM(LOWER(:specialty))
""")
    List<Object[]> findDoctorWithScheduleBySpecialty(@Param("specialty") String specialty);

    // Admin dashboard queries
    @Query("SELECT COUNT(u) FROM User u JOIN Role r ON r.user = u WHERE r.roleType = :roleType")
    long countByRoleType(@Param("roleType") String roleType);

    long countByIsEmailVerified(boolean isEmailVerified);
}
