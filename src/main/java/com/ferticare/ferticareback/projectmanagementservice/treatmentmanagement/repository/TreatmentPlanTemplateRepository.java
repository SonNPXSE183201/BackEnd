package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.TreatmentPlanTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TreatmentPlanTemplateRepository extends JpaRepository<TreatmentPlanTemplate, UUID> {
<<<<<<< HEAD
    
    List<TreatmentPlanTemplate> findByNameContainingIgnoreCase(String name);
    
    // Tìm template theo treatment type (IUI, IVF, ICSI)
    TreatmentPlanTemplate findByTreatmentTypeIgnoreCaseAndIsActiveTrue(String treatmentType);
    
    // Tìm template theo tên
    TreatmentPlanTemplate findByNameIgnoreCase(String name);
    
    // Lấy tất cả template đang hoạt động
    List<TreatmentPlanTemplate> findByIsActiveTrueOrderByTreatmentType();
} 
=======

    List<TreatmentPlanTemplate> findByNameContainingIgnoreCase(String name);

    // Tìm template theo treatment type (IUI, IVF, ICSI)
    TreatmentPlanTemplate findByTreatmentTypeIgnoreCaseAndIsActiveTrue(String treatmentType);

    // Tìm template theo tên
    TreatmentPlanTemplate findByNameIgnoreCase(String name);

    // Lấy tất cả template đang hoạt động
    List<TreatmentPlanTemplate> findByIsActiveTrueOrderByTreatmentType();
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
