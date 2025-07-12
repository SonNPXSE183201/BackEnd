package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, UUID> {

    // ========== BASIC QUERY METHODS ==========
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Tìm theo bệnh nhân
    List<TreatmentPlan> findByPatientIdOrderByCreatedDateDesc(UUID patientId);

    // Tìm theo bác sĩ
    List<TreatmentPlan> findByDoctorIdOrderByCreatedDateDesc(UUID doctorId);

    // Tìm theo loại điều trị
    List<TreatmentPlan> findByTreatmentType(String treatmentType);

    // Tìm theo trạng thái
    List<TreatmentPlan> findByStatus(String status);

    // Tìm phác đồ đang hoạt động
    List<TreatmentPlan> findByStatusAndPatientId(String status, UUID patientId);

    // Tìm phác đồ hoàn thành theo bệnh nhân
    List<TreatmentPlan> findByPatientIdAndStatusOrderByEndDateDesc(UUID patientId, String status);

    // Tìm theo business identifier planId
    Optional<TreatmentPlan> findByPlanId(UUID planId);

    // Tìm theo template
    List<TreatmentPlan> findByTemplateId(UUID templateId);

    // ========== BUSINESS QUERY METHODS ==========
<<<<<<< HEAD
    
    // Tìm phác đồ có nguy cơ cao
    @Query("SELECT tp FROM TreatmentPlan tp " +
           "WHERE tp.riskFactors IS NOT NULL AND tp.riskFactors != '' " +
           "AND tp.status IN ('active', 'draft')")
=======

    // Tìm phác đồ có nguy cơ cao
    @Query("SELECT tp FROM TreatmentPlan tp " +
            "WHERE tp.riskFactors IS NOT NULL AND tp.riskFactors != '' " +
            "AND tp.status IN ('active', 'draft')")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    List<TreatmentPlan> findHighRiskPlans();

    // Thống kê theo chu kỳ điều trị (không sử dụng outdated fields)
    @Query("SELECT tp.treatmentCycle, " +
<<<<<<< HEAD
           "COUNT(tp) as totalPlans " +
           "FROM TreatmentPlan tp " +
           "WHERE tp.treatmentCycle IS NOT NULL AND tp.status = 'completed' " +
           "GROUP BY tp.treatmentCycle " +
           "ORDER BY tp.treatmentCycle")
=======
            "COUNT(tp) as totalPlans " +
            "FROM TreatmentPlan tp " +
            "WHERE tp.treatmentCycle IS NOT NULL AND tp.status = 'completed' " +
            "GROUP BY tp.treatmentCycle " +
            "ORDER BY tp.treatmentCycle")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    List<Object[]> getTreatmentCycleStatistics();

    // Note: Các statistics methods khác đã được loại bỏ cho MVP
    // Note: finalOutcome, progressPercentage, approvedBy fields không còn tồn tại
<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
