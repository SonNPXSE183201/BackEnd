package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.TreatmentPhaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TreatmentPhaseStatusRepository extends JpaRepository<TreatmentPhaseStatus, UUID> {

    // Tìm theo treatment plan
    List<TreatmentPhaseStatus> findByTreatmentPlanIdOrderByPhaseId(UUID treatmentPlanId);

    // Tìm theo treatment plan và status
    List<TreatmentPhaseStatus> findByTreatmentPlanIdAndStatus(UUID treatmentPlanId, String status);

    // Tìm theo phase
    List<TreatmentPhaseStatus> findByPhaseId(UUID phaseId);

    // Tìm status hiện tại của treatment plan
    @Query("SELECT tps FROM TreatmentPhaseStatus tps WHERE tps.treatmentPlanId = :treatmentPlanId AND tps.status = 'In Progress'")
    Optional<TreatmentPhaseStatus> findCurrentActivePhase(@Param("treatmentPlanId") UUID treatmentPlanId);

    // Đếm số phase đã hoàn thành
    @Query("SELECT COUNT(tps) FROM TreatmentPhaseStatus tps WHERE tps.treatmentPlanId = :treatmentPlanId AND tps.status = 'Completed'")
    Long countCompletedPhases(@Param("treatmentPlanId") UUID treatmentPlanId);

    // Đếm tổng số phase
    @Query("SELECT COUNT(tps) FROM TreatmentPhaseStatus tps WHERE tps.treatmentPlanId = :treatmentPlanId")
    Long countTotalPhases(@Param("treatmentPlanId") UUID treatmentPlanId);

    // Kiểm tra có thể tiến tới phase tiếp theo không
    @Query("SELECT COUNT(tps) > 0 FROM TreatmentPhaseStatus tps WHERE tps.treatmentPlanId = :treatmentPlanId AND tps.phaseId = :phaseId AND tps.status = 'Completed'")
    boolean isPhaseCompleted(@Param("treatmentPlanId") UUID treatmentPlanId, @Param("phaseId") UUID phaseId);

    // Tìm phase status theo treatment plan và phase
    Optional<TreatmentPhaseStatus> findByTreatmentPlanIdAndPhaseId(UUID treatmentPlanId, UUID phaseId);
}