package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.ClinicalResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicalResultRepository extends JpaRepository<ClinicalResult, UUID> {

    // ========== FIND BY BUSINESS ID ==========
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Tìm theo resultId (business ID)
    Optional<ClinicalResult> findByResultId(UUID resultId);

    // ========== BASIC QUERY METHODS ==========
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    // Tìm theo bệnh nhân
    List<ClinicalResult> findByPatientIdOrderByCreatedDateDesc(UUID patientId);

    // Tìm theo bác sĩ
    List<ClinicalResult> findByDoctorIdOrderByCreatedDateDesc(UUID doctorId);

    // Tìm theo bệnh nhân và bác sĩ
    List<ClinicalResult> findByPatientIdAndDoctorIdOrderByCreatedDateDesc(UUID patientId, UUID doctorId);

    // Tìm theo trạng thái hoàn thành
    List<ClinicalResult> findByIsCompleted(Boolean isCompleted);

    // Tìm theo chẩn đoán
    List<ClinicalResult> findByDiagnosisCode(String diagnosisCode);

    // Tìm theo khoảng thời gian
    List<ClinicalResult> findByExaminationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Tìm kết quả khám hoàn thành theo bệnh nhân
    Optional<ClinicalResult> findByPatientIdAndIsCompletedTrue(UUID patientId);

    // Thống kê theo chẩn đoán
    @Query("SELECT cr.diagnosisCode, cr.diagnosis, COUNT(cr) as count " +
<<<<<<< HEAD
           "FROM ClinicalResult cr " +
           "WHERE cr.diagnosisCode IS NOT NULL " +
           "GROUP BY cr.diagnosisCode, cr.diagnosis " +
           "ORDER BY count DESC")
=======
            "FROM ClinicalResult cr " +
            "WHERE cr.diagnosisCode IS NOT NULL " +
            "GROUP BY cr.diagnosisCode, cr.diagnosis " +
            "ORDER BY count DESC")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    List<Object[]> getDiagnosisStatistics();

    // Thống kê theo mức độ nghiêm trọng
    @Query("SELECT cr.severityLevel, COUNT(cr) as count " +
<<<<<<< HEAD
           "FROM ClinicalResult cr " +
           "WHERE cr.severityLevel IS NOT NULL " +
           "GROUP BY cr.severityLevel " +
           "ORDER BY count DESC")
=======
            "FROM ClinicalResult cr " +
            "WHERE cr.severityLevel IS NOT NULL " +
            "GROUP BY cr.severityLevel " +
            "ORDER BY count DESC")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    List<Object[]> getSeverityStatistics();

    // Thống kê theo bác sĩ
    @Query("SELECT cr.doctorId, COUNT(cr) as totalExaminations, " +
<<<<<<< HEAD
           "COUNT(CASE WHEN cr.isCompleted = true THEN 1 END) as completedExaminations " +
           "FROM ClinicalResult cr " +
           "WHERE cr.doctorId IS NOT NULL " +
           "GROUP BY cr.doctorId")
=======
            "COUNT(CASE WHEN cr.isCompleted = true THEN 1 END) as completedExaminations " +
            "FROM ClinicalResult cr " +
            "WHERE cr.doctorId IS NOT NULL " +
            "GROUP BY cr.doctorId")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    List<Object[]> getDoctorStatistics();

    // Thống kê theo thời gian
    @Query("SELECT DATE(cr.examinationDate) as examDate, COUNT(cr) as count " +
<<<<<<< HEAD
           "FROM ClinicalResult cr " +
           "WHERE cr.examinationDate BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(cr.examinationDate) " +
           "ORDER BY examDate")
    List<Object[]> getDailyStatistics(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);

    // Tìm bệnh nhân có kết quả khám trong khoảng thời gian
    @Query("SELECT DISTINCT cr.patientId " +
           "FROM ClinicalResult cr " +
           "WHERE cr.examinationDate BETWEEN :startDate AND :endDate")
    List<UUID> findPatientIdsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);

    // Thống kê theo độ tuổi (cần join với bảng users)
    @Query("SELECT " +
           "CASE " +
           "  WHEN DATEDIFF(YEAR, u.dateOfBirth, CURRENT_DATE) < 30 THEN '18-29' " +
           "  WHEN DATEDIFF(YEAR, u.dateOfBirth, CURRENT_DATE) < 35 THEN '30-34' " +
           "  WHEN DATEDIFF(YEAR, u.dateOfBirth, CURRENT_DATE) < 40 THEN '35-39' " +
           "  ELSE '40+' " +
           "END as ageGroup, " +
           "COUNT(cr) as count " +
           "FROM ClinicalResult cr " +
           "JOIN User u ON cr.patientId = u.id " +
           "GROUP BY ageGroup " +
           "ORDER BY ageGroup")
    List<Object[]> getAgeGroupStatistics();

            // Note: Complications tracking not implemented yet

    // Thống kê theo loại kết quả
    @Query("SELECT cr.resultType, COUNT(cr) as count " +
           "FROM ClinicalResult cr " +
           "GROUP BY cr.resultType " +
           "ORDER BY count DESC")
    List<Object[]> getResultTypeStatistics();
} 
=======
            "FROM ClinicalResult cr " +
            "WHERE cr.examinationDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(cr.examinationDate) " +
            "ORDER BY examDate")
    List<Object[]> getDailyStatistics(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    // Tìm bệnh nhân có kết quả khám trong khoảng thời gian
    @Query("SELECT DISTINCT cr.patientId " +
            "FROM ClinicalResult cr " +
            "WHERE cr.examinationDate BETWEEN :startDate AND :endDate")
    List<UUID> findPatientIdsByDateRange(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    // Thống kê theo độ tuổi (cần join với bảng users)
    @Query("SELECT " +
            "CASE " +
            "  WHEN DATEDIFF(YEAR, u.dateOfBirth, CURRENT_DATE) < 30 THEN '18-29' " +
            "  WHEN DATEDIFF(YEAR, u.dateOfBirth, CURRENT_DATE) < 35 THEN '30-34' " +
            "  WHEN DATEDIFF(YEAR, u.dateOfBirth, CURRENT_DATE) < 40 THEN '35-39' " +
            "  ELSE '40+' " +
            "END as ageGroup, " +
            "COUNT(cr) as count " +
            "FROM ClinicalResult cr " +
            "JOIN User u ON cr.patientId = u.id " +
            "GROUP BY ageGroup " +
            "ORDER BY ageGroup")
    List<Object[]> getAgeGroupStatistics();

    // Note: Complications tracking not implemented yet

    // Thống kê theo loại kết quả
    @Query("SELECT cr.resultType, COUNT(cr) as count " +
            "FROM ClinicalResult cr " +
            "GROUP BY cr.resultType " +
            "ORDER BY count DESC")
    List<Object[]> getResultTypeStatistics();
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
