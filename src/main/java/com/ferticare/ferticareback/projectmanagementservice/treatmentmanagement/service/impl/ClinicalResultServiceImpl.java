package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.configuration.exception.ResourceNotFoundException;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.ClinicalResultRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.ClinicalResultResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.ClinicalResult;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.repository.ClinicalResultRepository;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.service.ClinicalResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClinicalResultServiceImpl implements ClinicalResultService {

    private final ClinicalResultRepository clinicalResultRepository;

    // Valid result types theo database CHECK constraint từ file SQL
    private final Set<String> VALID_RESULT_TYPES = Set.of(
<<<<<<< HEAD
        "Other", "Text", "Image", "Lab"  // BỔ SUNG: Thêm "Image" và "Lab" theo database
=======
            "Other", "Text", "Image", "Lab"  // BỔ SUNG: Thêm "Image" và "Lab" theo database
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    );

    private boolean isValidResultType(String resultType) {
        return resultType != null && VALID_RESULT_TYPES.contains(resultType);
    }

    private String validateAndFixResultType(String resultType) {
        if (isValidResultType(resultType)) {
            return resultType;
        }
        log.warn("Invalid result type '{}', using 'Other' as default", resultType);
        return "Other";
    }

    @Override
    public ClinicalResultResponse createClinicalResult(ClinicalResultRequest request) {
        log.info("Creating clinical result for patient: {}", request.getPatientId());
        ClinicalResult clinicalResult = new ClinicalResult();
        BeanUtils.copyProperties(request, clinicalResult);
        clinicalResult.setResultId(UUID.randomUUID());
        // Không còn xử lý visitId, chỉ cần appointmentId, patientId, doctorId
        // Validate và set result type
        clinicalResult.setResultType(validateAndFixResultType(clinicalResult.getResultType()));
        if (request.getSymptoms() != null && !request.getSymptoms().isEmpty()) {
            clinicalResult.setSymptoms(request.getSymptoms());
        }
        if (request.getWeight() != null && request.getHeight() != null) {
            BigDecimal heightInMeters = request.getHeight().divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal bmi = request.getWeight().divide(heightInMeters.multiply(heightInMeters), 2, BigDecimal.ROUND_HALF_UP);
            clinicalResult.setBmi(bmi);
        }
        if (clinicalResult.getExaminationDate() == null) {
            clinicalResult.setExaminationDate(LocalDateTime.now());
        }
        if (clinicalResult.getIsCompleted() == null) {
            clinicalResult.setIsCompleted(false);
        }
        ClinicalResult savedResult = clinicalResultRepository.save(clinicalResult);
        log.info("Clinical result created with ID: {} for appointment: {}", savedResult.getResultId(), savedResult.getAppointmentId());
        return convertToResponse(savedResult);
    }

    @Override
    public ClinicalResultResponse createClinicalResultWithDoctor(ClinicalResultRequest request, String doctorId) {
        log.info("Creating clinical result for patient: {} by doctor: {}", request.getPatientId(), doctorId);
        ClinicalResult clinicalResult = new ClinicalResult();
        BeanUtils.copyProperties(request, clinicalResult);
        clinicalResult.setResultId(UUID.randomUUID());
        clinicalResult.setDoctorId(UUID.fromString(doctorId));
        // Không còn xử lý visitId, chỉ cần appointmentId, patientId, doctorId
        clinicalResult.setResultType(validateAndFixResultType(clinicalResult.getResultType()));
        if (request.getSymptoms() != null && !request.getSymptoms().isEmpty()) {
            clinicalResult.setSymptoms(request.getSymptoms());
        }
        if (request.getWeight() != null && request.getHeight() != null) {
            BigDecimal heightInMeters = request.getHeight().divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal bmi = request.getWeight().divide(heightInMeters.multiply(heightInMeters), 2, BigDecimal.ROUND_HALF_UP);
            clinicalResult.setBmi(bmi);
        }
        if (clinicalResult.getExaminationDate() == null) {
            clinicalResult.setExaminationDate(LocalDateTime.now());
        }
        if (clinicalResult.getIsCompleted() == null) {
            clinicalResult.setIsCompleted(false);
        }
        ClinicalResult savedResult = clinicalResultRepository.save(clinicalResult);
        log.info("Clinical result created with ID: {} for appointment: {}", savedResult.getResultId(), savedResult.getAppointmentId());
        return convertToResponse(savedResult);
    }

    @Override
    public ClinicalResultResponse getClinicalResultById(String resultId) {
        log.info("Getting clinical result by ID: {}", resultId);
        ClinicalResult clinicalResult = clinicalResultRepository.findById(UUID.fromString(resultId))
                .orElseThrow(() -> new ResourceNotFoundException("Clinical result not found with ID: " + resultId));
        return convertToResponse(clinicalResult);
    }

    @Override
    public ClinicalResultResponse updateClinicalResult(String resultId, ClinicalResultRequest request, String userId, String role) {
        log.info("Updating clinical result with ID: {} by user: {}", resultId, userId);
        ClinicalResult existingResult = clinicalResultRepository.findById(UUID.fromString(resultId))
                .orElseThrow(() -> new ResourceNotFoundException("Clinical result not found with ID: " + resultId));

        // Access control: chỉ bác sĩ điều trị mới được cập nhật
        if (!(role.contains("DOCTOR") && existingResult.getDoctorId().equals(UUID.fromString(userId)))) {
            throw new AccessDeniedException("Chỉ bác sĩ điều trị mới có quyền cập nhật kết quả khám lâm sàng này.");
        }

        UUID oldAppointmentId = existingResult.getAppointmentId();
        UUID oldDoctorId = existingResult.getDoctorId();
        BeanUtils.copyProperties(request, existingResult, "resultId", "createdDate", "createdBy", "appointmentId", "doctorId");
        existingResult.setAppointmentId(oldAppointmentId);
        existingResult.setDoctorId(oldDoctorId);
        // Khi cập nhật, luôn set isCompleted = true (đã hoàn thành)
        existingResult.setIsCompleted(true);
        // Validate lại resultType để tránh lỗi CHECK constraint
        existingResult.setResultType(validateAndFixResultType(request.getResultType()));
        // Tính lại BMI nếu có thay đổi weight hoặc height
        if (request.getWeight() != null && request.getHeight() != null) {
            BigDecimal heightInMeters = request.getHeight().divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal bmi = request.getWeight().divide(heightInMeters.multiply(heightInMeters), 2, BigDecimal.ROUND_HALF_UP);
            existingResult.setBmi(bmi);
        }
        ClinicalResult updatedResult = clinicalResultRepository.save(existingResult);
        log.info("Clinical result updated successfully");
        return convertToResponse(updatedResult);
    }

    @Override
    public void deleteClinicalResult(String resultId) {
        log.info("Deleting clinical result with ID: {}", resultId);
        UUID uuid = UUID.fromString(resultId);
        if (!clinicalResultRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("Clinical result not found with ID: " + resultId);
        }
        clinicalResultRepository.deleteById(uuid);
        log.info("Clinical result deleted successfully");
    }

    @Override
    public List<ClinicalResultResponse> getClinicalResultsByPatient(UUID patientId) {
        log.info("Getting clinical results for patient: {}", patientId);
<<<<<<< HEAD
        
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        List<ClinicalResult> results = clinicalResultRepository.findByPatientIdOrderByCreatedDateDesc(patientId);
        return results.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClinicalResultResponse> getClinicalResultsByDoctor(String doctorId) {
        log.info("Getting clinical results for doctor: {}", doctorId);
<<<<<<< HEAD
        
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        List<ClinicalResult> results = clinicalResultRepository.findByDoctorIdOrderByCreatedDateDesc(UUID.fromString(doctorId));
        return results.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClinicalResultResponse> getCompletedClinicalResults() {
        log.info("Getting all completed clinical results");
<<<<<<< HEAD
        
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        List<ClinicalResult> results = clinicalResultRepository.findByIsCompleted(true);
        return results.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClinicalResultResponse> getClinicalResultsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Getting clinical results between {} and {}", startDate, endDate);
<<<<<<< HEAD
        
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        List<ClinicalResult> results = clinicalResultRepository.findByExaminationDateBetween(startDate, endDate);
        return results.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClinicalResultResponse getLatestCompletedResultByPatient(UUID patientId) {
        log.info("Getting latest completed clinical result for patient: {}", patientId);
<<<<<<< HEAD
        
        ClinicalResult result = clinicalResultRepository.findByPatientIdAndIsCompletedTrue(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("No completed clinical result found for patient: " + patientId));
        
=======

        ClinicalResult result = clinicalResultRepository.findByPatientIdAndIsCompletedTrue(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("No completed clinical result found for patient: " + patientId));

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        return convertToResponse(result);
    }

    @Override
    public Map<String, Object> getDiagnosisStatistics() {
        log.info("Getting diagnosis statistics");
<<<<<<< HEAD
        
        List<Object[]> statistics = clinicalResultRepository.getDiagnosisStatistics();
        Map<String, Object> result = new HashMap<>();
        
=======

        List<Object[]> statistics = clinicalResultRepository.getDiagnosisStatistics();
        Map<String, Object> result = new HashMap<>();

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        List<Map<String, Object>> diagnosisData = statistics.stream()
                .map(row -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("diagnosisCode", row[0]);
                    data.put("diagnosis", row[1]);
                    data.put("count", row[2]);
                    return data;
                })
                .collect(Collectors.toList());
<<<<<<< HEAD
        
        result.put("diagnosisStatistics", diagnosisData);
        result.put("totalDiagnoses", diagnosisData.size());
        
=======

        result.put("diagnosisStatistics", diagnosisData);
        result.put("totalDiagnoses", diagnosisData.size());

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        return result;
    }

    @Override
    public Map<String, Object> getSeverityStatistics() {
        log.info("Getting severity statistics");
<<<<<<< HEAD
        
        List<Object[]> statistics = clinicalResultRepository.getSeverityStatistics();
        Map<String, Object> result = new HashMap<>();
        
=======

        List<Object[]> statistics = clinicalResultRepository.getSeverityStatistics();
        Map<String, Object> result = new HashMap<>();

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        List<Map<String, Object>> severityData = statistics.stream()
                .map(row -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("severityLevel", row[0]);
                    data.put("count", row[1]);
                    return data;
                })
                .collect(Collectors.toList());
<<<<<<< HEAD
        
        result.put("severityStatistics", severityData);
        result.put("totalSeverityLevels", severityData.size());
        
=======

        result.put("severityStatistics", severityData);
        result.put("totalSeverityLevels", severityData.size());

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        return result;
    }

    @Override
    public Map<String, Object> getDoctorStatistics() {
        log.info("Getting doctor statistics");
<<<<<<< HEAD
        
        List<Object[]> statistics = clinicalResultRepository.getDoctorStatistics();
        Map<String, Object> result = new HashMap<>();
        
=======

        List<Object[]> statistics = clinicalResultRepository.getDoctorStatistics();
        Map<String, Object> result = new HashMap<>();

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        List<Map<String, Object>> doctorData = statistics.stream()
                .map(row -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("doctorId", row[0]);
                    data.put("totalExaminations", row[1]);
                    data.put("completedExaminations", row[2]);
                    return data;
                })
                .collect(Collectors.toList());
<<<<<<< HEAD
        
        result.put("doctorStatistics", doctorData);
        result.put("totalDoctors", doctorData.size());
        
=======

        result.put("doctorStatistics", doctorData);
        result.put("totalDoctors", doctorData.size());

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        return result;
    }

    @Override
    public Map<String, Object> getDailyStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Getting daily statistics between {} and {}", startDate, endDate);
<<<<<<< HEAD
        
        List<Object[]> statistics = clinicalResultRepository.getDailyStatistics(startDate, endDate);
        Map<String, Object> result = new HashMap<>();
        
=======

        List<Object[]> statistics = clinicalResultRepository.getDailyStatistics(startDate, endDate);
        Map<String, Object> result = new HashMap<>();

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        List<Map<String, Object>> dailyData = statistics.stream()
                .map(row -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("examDate", row[0]);
                    data.put("count", row[1]);
                    return data;
                })
                .collect(Collectors.toList());
<<<<<<< HEAD
        
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        result.put("dailyStatistics", dailyData);
        result.put("totalDays", dailyData.size());
        result.put("startDate", startDate);
        result.put("endDate", endDate);
<<<<<<< HEAD
        
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        return result;
    }

    @Override
    public Map<String, Object> getAgeGroupStatistics() {
        log.info("Getting age group statistics");
<<<<<<< HEAD
        
        List<Object[]> statistics = clinicalResultRepository.getAgeGroupStatistics();
        Map<String, Object> result = new HashMap<>();
        
=======

        List<Object[]> statistics = clinicalResultRepository.getAgeGroupStatistics();
        Map<String, Object> result = new HashMap<>();

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        List<Map<String, Object>> ageData = statistics.stream()
                .map(row -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("ageGroup", row[0]);
                    data.put("count", row[1]);
                    return data;
                })
                .collect(Collectors.toList());
<<<<<<< HEAD
        
        result.put("ageGroupStatistics", ageData);
        result.put("totalAgeGroups", ageData.size());
        
=======

        result.put("ageGroupStatistics", ageData);
        result.put("totalAgeGroups", ageData.size());

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        return result;
    }

    @Override
    public Map<String, Object> getResultTypeStatistics() {
        log.info("Getting result type statistics");
<<<<<<< HEAD
        
        List<Object[]> statistics = clinicalResultRepository.getResultTypeStatistics();
        Map<String, Object> result = new HashMap<>();
        
=======

        List<Object[]> statistics = clinicalResultRepository.getResultTypeStatistics();
        Map<String, Object> result = new HashMap<>();

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        List<Map<String, Object>> typeData = statistics.stream()
                .map(row -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("resultType", row[0]);
                    data.put("count", row[1]);
                    return data;
                })
                .collect(Collectors.toList());
<<<<<<< HEAD
        
        result.put("resultTypeStatistics", typeData);
        result.put("totalResultTypes", typeData.size());
        
=======

        result.put("resultTypeStatistics", typeData);
        result.put("totalResultTypes", typeData.size());

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        return result;
    }

    @Override
    public Map<String, Object> getComplicationStatistics() {
        log.info("Getting complication statistics");
<<<<<<< HEAD
        
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Complications tracking not implemented yet");
        result.put("totalResultsWithComplications", 0);
        result.put("complicationDetails", new ArrayList<>());
<<<<<<< HEAD
        
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        return result;
    }

    @Override
    public Map<String, Object> getSuccessRateByDiagnosis() {
        // Implementation for advanced statistics
        log.info("Getting success rate by diagnosis");
        Map<String, Object> result = new HashMap<>();
        // TODO: Implement based on treatment outcomes
        return result;
    }

    @Override
    public Map<String, Object> getTreatmentRecommendationStatistics() {
        log.info("Getting treatment recommendation statistics");
        Map<String, Object> result = new HashMap<>();
        // TODO: Implement based on treatment recommendations
        return result;
    }

    @Override
    public Map<String, Object> getPatientDemographics() {
        log.info("Getting patient demographics");
        Map<String, Object> result = new HashMap<>();
        // TODO: Implement patient demographics analysis
        return result;
    }

    @Override
    public Map<String, Object> getExaminationTrends(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Getting examination trends between {} and {}", startDate, endDate);
        Map<String, Object> result = new HashMap<>();
        // TODO: Implement examination trends analysis
        return result;
    }

    @Override
    public ClinicalResultResponse getClinicalResultByIdWithAccessCheck(UUID resultId, String userId, String role) {
        ClinicalResult clinicalResult = clinicalResultRepository.findById(resultId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinical result not found with ID: " + resultId));
<<<<<<< HEAD
        
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        // ✅ CUSTOMER chỉ xem được ClinicalResult của chính họ
        if (role.contains("CUSTOMER")) {
            if (!clinicalResult.getPatientId().equals(UUID.fromString(userId))) {
                throw new AccessDeniedException("Bạn không có quyền xem kết quả khám bệnh của người khác. Chỉ có thể xem kết quả khám bệnh của chính mình.");
            }
        }
        // ✅ DOCTOR chỉ xem được ClinicalResult của bệnh nhân họ điều trị
        else if (role.contains("DOCTOR")) {
            if (!clinicalResult.getDoctorId().equals(UUID.fromString(userId))) {
                throw new AccessDeniedException("Bạn không có quyền xem kết quả khám bệnh của bệnh nhân khác. Chỉ có thể xem kết quả khám bệnh của bệnh nhân mà bạn điều trị.");
            }
        }
        // ✅ MANAGER và ADMIN có thể xem tất cả
        else if (!role.contains("MANAGER") && !role.contains("ADMIN")) {
            throw new AccessDeniedException("Bạn không có quyền truy cập kết quả khám bệnh.");
        }
<<<<<<< HEAD
        
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        return convertToResponse(clinicalResult);
    }

    @Override
    public List<ClinicalResultResponse> getClinicalResultsByPatientWithAccessCheck(UUID patientId, String userId, String role) {
        // ✅ CUSTOMER chỉ xem được ClinicalResult của chính họ
        if (role.contains("CUSTOMER")) {
            if (!patientId.equals(UUID.fromString(userId))) {
                throw new AccessDeniedException("Bạn không có quyền xem kết quả khám bệnh của người khác. Chỉ có thể xem kết quả khám bệnh của chính mình.");
            }
            List<ClinicalResult> results = clinicalResultRepository.findByPatientIdOrderByCreatedDateDesc(patientId);
            return results.stream().map(this::convertToResponse).toList();
        }
        // ✅ DOCTOR chỉ xem được ClinicalResult của bệnh nhân họ điều trị
        else if (role.contains("DOCTOR")) {
            List<ClinicalResult> results = clinicalResultRepository.findByPatientIdAndDoctorIdOrderByCreatedDateDesc(patientId, UUID.fromString(userId));
            return results.stream().map(this::convertToResponse).toList();
        }
        // ✅ MANAGER và ADMIN có thể xem tất cả
        else if (role.contains("MANAGER") || role.contains("ADMIN")) {
            List<ClinicalResult> results = clinicalResultRepository.findByPatientIdOrderByCreatedDateDesc(patientId);
            return results.stream().map(this::convertToResponse).toList();
        }
        else {
            throw new AccessDeniedException("Bạn không có quyền truy cập kết quả khám bệnh.");
        }
    }

    private ClinicalResultResponse convertToResponse(ClinicalResult clinicalResult) {
        ClinicalResultResponse response = new ClinicalResultResponse();
        BeanUtils.copyProperties(clinicalResult, response);
        return response;
    }
<<<<<<< HEAD
} 
=======
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
