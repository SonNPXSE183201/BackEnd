package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.ClinicalResultRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.ClinicalResultResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.ClinicalResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ClinicalResultService {

    // CRUD operations
    ClinicalResultResponse createClinicalResult(ClinicalResultRequest request);
    ClinicalResultResponse createClinicalResultWithDoctor(ClinicalResultRequest request, String doctorId);
    ClinicalResultResponse getClinicalResultById(String resultId);
    ClinicalResultResponse updateClinicalResult(String resultId, ClinicalResultRequest request, String userId, String role);
    void deleteClinicalResult(String resultId);

    // Query operations
    List<ClinicalResultResponse> getClinicalResultsByPatient(UUID patientId);
    List<ClinicalResultResponse> getClinicalResultsByDoctor(String doctorId);
    List<ClinicalResultResponse> getCompletedClinicalResults();
    List<ClinicalResultResponse> getClinicalResultsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    ClinicalResultResponse getLatestCompletedResultByPatient(UUID patientId);

    // Statistics operations
    Map<String, Object> getDiagnosisStatistics();
    Map<String, Object> getSeverityStatistics();
    Map<String, Object> getDoctorStatistics();
    Map<String, Object> getDailyStatistics(LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> getAgeGroupStatistics();
    Map<String, Object> getResultTypeStatistics();
    Map<String, Object> getComplicationStatistics();

    // Advanced statistics
    Map<String, Object> getSuccessRateByDiagnosis();
    Map<String, Object> getTreatmentRecommendationStatistics();
    Map<String, Object> getPatientDemographics();
    Map<String, Object> getExaminationTrends(LocalDateTime startDate, LocalDateTime endDate);

    ClinicalResultResponse getClinicalResultByIdWithAccessCheck(UUID resultId, String userId, String role);
    List<ClinicalResultResponse> getClinicalResultsByPatientWithAccessCheck(UUID patientId, String userId, String role);
} 