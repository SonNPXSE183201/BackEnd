package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
    
    @Query("SELECT d FROM Department d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Department> findByNameOrDescriptionContaining(@Param("search") String search, Pageable pageable);
    
    List<Department> findByIsActive(Boolean isActive);
} 