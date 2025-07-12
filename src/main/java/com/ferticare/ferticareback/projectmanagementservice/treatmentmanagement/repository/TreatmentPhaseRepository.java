package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.TreatmentPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TreatmentPhaseRepository extends JpaRepository<TreatmentPhase, UUID> {

    // Tìm theo service
    List<TreatmentPhase> findByServiceIdOrderByPhaseOrder(UUID serviceId);

    // Tìm theo tên phase
    List<TreatmentPhase> findByPhaseNameContaining(String phaseName);

    // Tìm phase theo thứ tự
    Optional<TreatmentPhase> findByServiceIdAndPhaseOrder(UUID serviceId, Integer phaseOrder);

    // Lấy phase tiếp theo
    @Query("SELECT tp FROM TreatmentPhase tp WHERE tp.serviceId = :serviceId AND tp.phaseOrder > :currentOrder ORDER BY tp.phaseOrder LIMIT 1")
    Optional<TreatmentPhase> findNextPhase(@Param("serviceId") UUID serviceId, @Param("currentOrder") Integer currentOrder);

    // Lấy phase đầu tiên của service
    @Query("SELECT tp FROM TreatmentPhase tp WHERE tp.serviceId = :serviceId ORDER BY tp.phaseOrder LIMIT 1")
    Optional<TreatmentPhase> findFirstPhase(@Param("serviceId") UUID serviceId);

    // Lấy phase cuối cùng của service
    @Query("SELECT tp FROM TreatmentPhase tp WHERE tp.serviceId = :serviceId ORDER BY tp.phaseOrder DESC LIMIT 1")
    Optional<TreatmentPhase> findLastPhase(@Param("serviceId") UUID serviceId);

    // Đếm số phase của service
    @Query("SELECT COUNT(tp) FROM TreatmentPhase tp WHERE tp.serviceId = :serviceId")
    Long countPhasesByService(@Param("serviceId") UUID serviceId);

    // Tìm phases có duration lớn hơn threshold
    @Query("SELECT tp FROM TreatmentPhase tp WHERE tp.expectedDuration > :minDuration")
    List<TreatmentPhase> findLongDurationPhases(@Param("minDuration") Integer minDuration);
<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
