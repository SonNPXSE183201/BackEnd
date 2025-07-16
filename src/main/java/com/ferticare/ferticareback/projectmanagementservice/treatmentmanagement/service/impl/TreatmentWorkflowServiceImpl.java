package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.configuration.exception.ResourceNotFoundException;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.ClinicalResultRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.TreatmentPlanRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.TreatmentStepDTO;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.PhaseStatusUpdateRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.ClinicalResultResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.TreatmentPlanResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.PhaseStatusResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.entity.*;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.repository.*;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.service.*;
import com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.service.EmailService;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.Appointment;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.AppointmentRepository;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.entity.User;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.MedicationPlanDTO;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.MonitoringScheduleDTO;
import com.ferticare.ferticareback.projectmanagementservice.profile.repository.ProfileRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TreatmentWorkflowServiceImpl implements TreatmentWorkflowService {

    private final ClinicalResultService clinicalResultService;
    private final EmailService emailService;
    
    private final ClinicalResultRepository clinicalResultRepository;
    private final TreatmentPlanRepository treatmentPlanRepository;
    private final TreatmentPhaseStatusRepository treatmentPhaseStatusRepository;
    private final TreatmentPhaseRepository treatmentPhaseRepository;
    private final TreatmentPlanTemplateRepository treatmentPlanTemplateRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ========== WORKFLOW CHÍNH ==========

    @Override
    public TreatmentPlanResponse createTreatmentPlanFromClinicalResult(String clinicalResultId, TreatmentPlanRequest request, String doctorId) {
        log.info("Creating treatment plan from clinical result: {} by doctor: {}", clinicalResultId, doctorId);
        
        // Lấy clinical result
        ClinicalResultResponse clinicalResult = clinicalResultService.getClinicalResultById(clinicalResultId);
        if (clinicalResult == null) {
            throw new ResourceNotFoundException("Clinical result not found: " + clinicalResultId);
        }
        
        // Set thông tin từ clinical result
        request.setPatientId(clinicalResult.getPatientId());
        
        // Merge thông tin từ template vào request
        mergeTemplateIntoRequest(request, doctorId);
        
        // Tạo treatment plan
        TreatmentPlanResponse treatmentPlan = createTreatmentPlanWithDoctor(request, doctorId);
        
        // Gửi email thông báo cho bệnh nhân
        sendTreatmentPhasesEmail(treatmentPlan.getPlanId());
        
        log.info("Treatment plan created successfully with phases and email notification: {}", treatmentPlan.getPlanId());
        return treatmentPlan;
    }

    @Override
    public TreatmentPlanResponse modifyTreatmentPlan(UUID treatmentPlanId, TreatmentPlanRequest modifications, String doctorId) {
        log.info("Modifying treatment plan: {} by doctor: {}", treatmentPlanId, doctorId);
        TreatmentPlanResponse updatedPlan = updateTreatmentPlan(treatmentPlanId, modifications);
        log.info("Treatment plan modified successfully: {}", treatmentPlanId);
        return updatedPlan;
    }

    @Override
    public void completeTreatmentPlan(UUID treatmentPlanId, String notes, String doctorId) {
        log.info("Completing treatment plan: {} with notes: {} by doctor: {}", treatmentPlanId, notes, doctorId);
        
        // Cập nhật treatment plan status
        TreatmentPlan plan = treatmentPlanRepository.findById(treatmentPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found: " + treatmentPlanId));
        
        plan.setStatus("completed");
        plan.setEndDate(LocalDateTime.now());
        plan.setUpdatedBy(doctorId);
        
        treatmentPlanRepository.save(plan);
        
        // Gửi email thông báo hoàn thành
        sendTreatmentCompletionEmail(treatmentPlanId);
        
        log.info("Treatment plan completed successfully: {}", treatmentPlanId);
    }

    @Override
    public void cancelTreatmentPlan(UUID treatmentPlanId, String reason, String doctorId) {
        log.info("Cancelling treatment plan: {} with reason: {} by doctor: {}", treatmentPlanId, reason, doctorId);
        
        TreatmentPlan plan = treatmentPlanRepository.findById(treatmentPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found: " + treatmentPlanId));
        
        plan.setStatus("cancelled");
        plan.setUpdatedBy(doctorId);
        treatmentPlanRepository.save(plan);

        // Cập nhật trạng thái tất cả các phase thành Cancelled
        List<TreatmentPhaseStatus> phaseStatuses = treatmentPhaseStatusRepository.findByTreatmentPlanIdOrderByPhaseId(treatmentPlanId);
        for (TreatmentPhaseStatus phaseStatus : phaseStatuses) {
            phaseStatus.setStatus("Cancelled");
            treatmentPhaseStatusRepository.save(phaseStatus);
        }

        // Gửi mail hủy lịch cho bệnh nhân với template màu hồng, có thông tin liên hệ
        try {
            Optional<User> patientOpt = userRepository.findById(plan.getPatientId());
            Optional<User> doctorOpt = plan.getDoctorId() != null ? userRepository.findById(plan.getDoctorId()) : Optional.empty();
            if (patientOpt.isPresent()) {
                User patient = patientOpt.get();
                User doctor = doctorOpt.orElse(null);
                emailService.sendTreatmentCancelled(patient, doctor, plan, reason);
            }
        } catch (Exception e) {
            log.error("Lỗi khi gửi mail hủy kế hoạch điều trị: {}", e.getMessage());
        }

        log.info("Treatment plan and all phases cancelled successfully: {}", treatmentPlanId);
    }

    @Override
    public TreatmentPlanResponse recreateTreatmentPlanFromHistory(UUID patientId, UUID previousTreatmentPlanId, String doctorId) {
        log.info("Recreating treatment plan for patient: {} from previous plan: {} by doctor: {}", patientId, previousTreatmentPlanId, doctorId);
        
        // Lấy thông tin phác đồ cũ
        TreatmentPlanResponse previousPlan = getTreatmentPlanById(previousTreatmentPlanId);
        if (previousPlan == null) {
            throw new ResourceNotFoundException("Previous treatment plan not found: " + previousTreatmentPlanId);
        }
        
        // Tạo request mới từ thông tin cũ
        TreatmentPlanRequest newRequest = createRequestFromPreviousPlan(previousPlan);
        newRequest.setPatientId(patientId);
        
        // Tạo phác đồ mới
        TreatmentPlanResponse newPlan = createTreatmentPlanWithDoctor(newRequest, doctorId);
        
        log.info("New treatment plan created from history: {}", newPlan.getPlanId());
        return newPlan;
    }

    // ========== CRUD OPERATIONS (from TreatmentPlanService) ==========

    @Override
    public TreatmentPlanResponse createTreatmentPlan(TreatmentPlanRequest request) {
        log.info("Creating treatment plan for patient: {}", request.getPatientId());
        
        TreatmentPlan treatmentPlan = new TreatmentPlan();
        org.springframework.beans.BeanUtils.copyProperties(request, treatmentPlan);
        
        // Generate UUID and sync both ID fields
        UUID generatedPlanId = UUID.randomUUID();
        treatmentPlan.setPlanId(generatedPlanId);
        
        // Chuyển đổi List sang JSON string cho các trường JSON
        try {
            if (request.getTreatmentSteps() != null) {
                treatmentPlan.setTreatmentSteps(objectMapper.writeValueAsString(request.getTreatmentSteps()));
            }
            if (request.getMedicationPlan() != null) {
                treatmentPlan.setMedicationPlan(objectMapper.writeValueAsString(request.getMedicationPlan()));
            }
            if (request.getMonitoringSchedule() != null) {
                treatmentPlan.setMonitoringSchedule(objectMapper.writeValueAsString(request.getMonitoringSchedule()));
            }
        } catch (Exception e) {
            log.error("Error converting List to JSON string: {}", e.getMessage());
            throw new RuntimeException("Error processing treatment plan data", e);
        }
        
        TreatmentPlan savedPlan = treatmentPlanRepository.save(treatmentPlan);
        log.info("Treatment plan created with ID: {} (planId: {})", savedPlan.getPlanId(), savedPlan.getPlanId());
        
        return convertToResponse(savedPlan);
    }

    @Override
    public TreatmentPlanResponse createTreatmentPlanWithDoctor(TreatmentPlanRequest request, String doctorId) {
        log.info("Creating treatment plan for patient: {} by doctor: {}", request.getPatientId(), doctorId);

        // Kiểm tra quyền bác sĩ trước khi tạo treatment plan
        String doctorSpecialty = getDoctorSpecialty(doctorId);
        log.info("Doctor {} with specialty {} requesting to create treatment plan for type: {}", 
                doctorId, doctorSpecialty, request.getTreatmentType());
        
        // Kiểm tra bác sĩ có quyền tạo treatment plan cho loại này không
        if (!canDoctorAccessTemplate(doctorSpecialty, request.getTreatmentType())) {
            throw new IllegalStateException(
                String.format("Bác sĩ có specialty '%s' không được phép tạo treatment plan cho loại '%s'. " +
                            "Chỉ bác sĩ IUI mới được tạo IUI, bác sĩ IVF mới được tạo IVF.", 
                            doctorSpecialty, request.getTreatmentType()));
        }

        // Kiểm tra 1 bệnh nhân chỉ có 1 plan active
        List<TreatmentPlan> activePlans = treatmentPlanRepository.findByStatusAndPatientId("active", request.getPatientId());
        if (!activePlans.isEmpty()) {
            throw new IllegalStateException("Bệnh nhân đã có phác đồ điều trị đang hoạt động!");
        }
        // Kiểm tra 1 bác sĩ chỉ tạo được 1 plan active cho 1 bệnh nhân
        List<TreatmentPlan> doctorPatientActivePlans = treatmentPlanRepository.findByStatusAndPatientId("active", request.getPatientId())
                .stream().filter(tp -> tp.getDoctorId() != null && tp.getDoctorId().toString().equals(doctorId)).toList();
        if (!doctorPatientActivePlans.isEmpty()) {
            throw new IllegalStateException("Bác sĩ đã có phác đồ điều trị active cho bệnh nhân này!");
        }

        TreatmentPlan treatmentPlan = new TreatmentPlan();
        org.springframework.beans.BeanUtils.copyProperties(request, treatmentPlan);
        
        // Generate UUID and sync both ID fields
        UUID generatedPlanId = UUID.randomUUID();
        treatmentPlan.setPlanId(generatedPlanId);
        
        // Tự động set các giá trị cần thiết
        treatmentPlan.setDoctorId(UUID.fromString(doctorId));
        // Không set createdBy/updatedBy ở đây, để JPA tự động qua BaseEntity
        // Set status = 'active' khi tạo từ template đã merge trong request
        if (treatmentPlan.getStartDate() == null) {
            treatmentPlan.setStartDate(LocalDateTime.now());
        }
        // Nếu endDate null thì set endDate = startDate + estimatedDurationDays + 1 ngày
        if (treatmentPlan.getEndDate() == null && treatmentPlan.getStartDate() != null && treatmentPlan.getEstimatedDurationDays() != null) {
            treatmentPlan.setEndDate(treatmentPlan.getStartDate().plusDays(treatmentPlan.getEstimatedDurationDays() + 1));
        }
        // Copy treatmentCycle từ request nếu có
        if (request.getTreatmentCycle() != null) {
            treatmentPlan.setTreatmentCycle(request.getTreatmentCycle());
        }
        
        // Chuyển đổi List sang JSON string cho các trường JSON
        try {
            if (request.getTreatmentSteps() != null) {
                treatmentPlan.setTreatmentSteps(objectMapper.writeValueAsString(request.getTreatmentSteps()));
            }
            if (request.getMedicationPlan() != null) {
                treatmentPlan.setMedicationPlan(objectMapper.writeValueAsString(request.getMedicationPlan()));
            }
            if (request.getMonitoringSchedule() != null) {
                treatmentPlan.setMonitoringSchedule(objectMapper.writeValueAsString(request.getMonitoringSchedule()));
            }
        } catch (Exception e) {
            log.error("Error converting List to JSON string: {}", e.getMessage());
            throw new RuntimeException("Error processing treatment plan data", e);
        }
        
        TreatmentPlan savedPlan = treatmentPlanRepository.save(treatmentPlan);
        log.info("Treatment plan created with ID: {} (planId: {})", savedPlan.getPlanId(), savedPlan.getPlanId());
        // Auto-generate phases from treatment_steps
        try {
            createTreatmentPhasesFromPlan(savedPlan);
        } catch (Exception e) {
            log.error("Error creating phases: {}", e.getMessage());
        }
        return convertToResponse(savedPlan);
    }

    @Override
    public TreatmentPlanResponse getTreatmentPlanById(UUID planId) {
        log.info("Getting treatment plan by ID: {}", planId);
        TreatmentPlan treatmentPlan = treatmentPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found with ID: " + planId));

        // Phân quyền: chỉ cho phép bệnh nhân xem plan của mình, bác sĩ xem plan của bệnh nhân mình điều trị, manager/admin xem tất cả
        // TODO: Lấy userId và role từ context/JWT (giả lập biến userId, userRole ở đây)
        String userId = "MOCK_USER_ID"; // TODO: lấy từ context
        String userRole = "MOCK_ROLE"; // TODO: lấy từ context
        if ("CUSTOMER".equalsIgnoreCase(userRole)) {
            if (treatmentPlan.getPatientId() == null || !treatmentPlan.getPatientId().toString().equals(userId)) {
                throw new SecurityException("Bạn không có quyền xem phác đồ điều trị này!");
            }
        } else if ("DOCTOR".equalsIgnoreCase(userRole)) {
            if (treatmentPlan.getDoctorId() == null || !treatmentPlan.getDoctorId().toString().equals(userId)) {
                throw new SecurityException("Bạn không có quyền xem phác đồ điều trị này!");
            }
        }
        // MANAGER, ADMIN: cho phép tất cả
        return convertToResponse(treatmentPlan);
    }

    @Override
    public TreatmentPlanResponse updateTreatmentPlan(UUID planId, TreatmentPlanRequest request) {
        log.info("Updating treatment plan: {}", planId);
        TreatmentPlan plan = treatmentPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found: " + planId));
        org.springframework.beans.BeanUtils.copyProperties(request, plan, "planId", "createdDate", "createdBy");
        // Không set createdDate/createdBy khi update, để JPA tự động
        // Set updatedDate/updatedBy sẽ tự động qua BaseEntity nếu bật auditing
        // Nếu endDate null thì set endDate = startDate + estimatedDurationDays + 1 ngày
        if (plan.getEndDate() == null && plan.getStartDate() != null && plan.getEstimatedDurationDays() != null) {
            plan.setEndDate(plan.getStartDate().plusDays(plan.getEstimatedDurationDays() + 1));
        }
        
        // Chuyển đổi List sang JSON string cho các trường JSON
        try {
            if (request.getTreatmentSteps() != null) {
                plan.setTreatmentSteps(objectMapper.writeValueAsString(request.getTreatmentSteps()));
            }
            if (request.getMedicationPlan() != null) {
                plan.setMedicationPlan(objectMapper.writeValueAsString(request.getMedicationPlan()));
            }
            if (request.getMonitoringSchedule() != null) {
                plan.setMonitoringSchedule(objectMapper.writeValueAsString(request.getMonitoringSchedule()));
            }
        } catch (Exception e) {
            log.error("Error converting List to JSON string: {}", e.getMessage());
            throw new RuntimeException("Error processing treatment plan data", e);
        }
        
        TreatmentPlan savedPlan = treatmentPlanRepository.save(plan);
        return convertToResponse(savedPlan);
    }

    @Override
    public void deleteTreatmentPlan(UUID planId) {
        log.info("Deleting treatment plan with ID: {}", planId);
        if (!treatmentPlanRepository.existsById(planId)) {
            throw new ResourceNotFoundException("Treatment plan not found with ID: " + planId);
        }
        treatmentPlanRepository.deleteById(planId);
        log.info("Treatment plan deleted successfully");
    }

    @Override
    public List<TreatmentPlanResponse> getTreatmentPlansByPatient(UUID patientId) {
        log.info("Getting treatment plans for patient: {}", patientId);
        // Phân quyền: chỉ cho phép bệnh nhân xem của mình, bác sĩ xem của bệnh nhân mình điều trị, manager/admin xem tất cả
        String userId = "MOCK_USER_ID"; // TODO: lấy từ context
        String userRole = "MOCK_ROLE"; // TODO: lấy từ context
        if ("CUSTOMER".equalsIgnoreCase(userRole)) {
            if (!patientId.toString().equals(userId)) {
                throw new SecurityException("Bạn không có quyền xem phác đồ điều trị này!");
            }
        }
        // Bác sĩ, manager, admin: cho phép xem danh sách
        List<TreatmentPlan> plans = treatmentPlanRepository.findByPatientIdOrderByCreatedDateDesc(patientId);
        return plans.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TreatmentPlanResponse> getTreatmentPlansByDoctor(String doctorId) {
        log.info("Getting treatment plans for doctor: {}", doctorId);
        String userId = "MOCK_USER_ID"; // TODO: lấy từ context
        String userRole = "MOCK_ROLE"; // TODO: lấy từ context
        if ("DOCTOR".equalsIgnoreCase(userRole)) {
            if (!doctorId.equals(userId)) {
                throw new SecurityException("Bạn không có quyền xem phác đồ điều trị này!");
            }
        }
        // Manager, admin: cho phép xem danh sách
        List<TreatmentPlan> plans = treatmentPlanRepository.findByDoctorIdOrderByCreatedDateDesc(UUID.fromString(doctorId));
        return plans.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TreatmentPlanResponse> getTreatmentPlansByType(String treatmentType) {
        log.info("Getting treatment plans by type: {}", treatmentType);
        
        List<TreatmentPlan> plans = treatmentPlanRepository.findByTreatmentType(treatmentType);
        return plans.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TreatmentPlanResponse approveTreatmentPlan(UUID planId, String approverId, String notes) {
        log.info("Changing treatment plan status to active: {} by doctor: {}", planId, approverId);
        TreatmentPlan treatmentPlan = treatmentPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found with ID: " + planId));
        treatmentPlan.setStatus("active");
        treatmentPlan.setUpdatedBy(approverId);
        TreatmentPlan activatedPlan = treatmentPlanRepository.save(treatmentPlan);
        log.info("Treatment plan activated successfully");
        return convertToResponse(activatedPlan);
    }

    // ========== QUERY OPERATIONS ==========

    @Override
    public List<ClinicalResultResponse> getPatientClinicalResults(UUID patientId) {
        log.info("Getting clinical results for patient: {}", patientId);
        return clinicalResultService.getClinicalResultsByPatient(patientId);
    }

    @Override
    public Map<String, Object> getPatientTreatmentPhases(UUID patientId) {
        log.info("Getting treatment phases for patient: {}", patientId);
        // Lấy active treatment plan
        List<TreatmentPlan> activePlans = treatmentPlanRepository.findByStatusAndPatientId("active", patientId);
        Map<String, Object> result = new HashMap<>();
        if (!activePlans.isEmpty()) {
            TreatmentPlan activePlan = activePlans.get(0);
            List<TreatmentPhaseStatus> phases = treatmentPhaseStatusRepository.findByTreatmentPlanIdOrderByPhaseId(activePlan.getPlanId());
            
            // Format bảng cho FE
            List<Map<String, Object>> tablePhases = phases.stream().map(phaseStatus -> {
                Map<String, Object> row = new HashMap<>();
                
                // FIX: Đổi tên field để tránh nhầm lẫn và JOIN để lấy tên thật
                row.put("phaseId", phaseStatus.getPhaseId()); // UUID của phase
                row.put("status", phaseStatus.getStatus());
                row.put("startDate", phaseStatus.getStartDate());
                row.put("endDate", phaseStatus.getEndDate());
                row.put("doctor", activePlan.getDoctorId());
                
                // JOIN với TreatmentPhase để lấy thông tin thật
                TreatmentPhase treatmentPhase = treatmentPhaseRepository.findById(phaseStatus.getPhaseId()).orElse(null);
                if (treatmentPhase != null) {
                    row.put("phaseName", treatmentPhase.getPhaseName()); // Tên thật từ DB
                    row.put("description", treatmentPhase.getDescription());
                    row.put("phaseOrder", treatmentPhase.getPhaseOrder());
                    row.put("expectedDuration", treatmentPhase.getExpectedDuration());
                    row.put("serviceId", treatmentPhase.getServiceId());
                } else {
                    // Fallback nếu không tìm thấy phase
                    row.put("phaseName", "Unknown Phase");
                    row.put("description", null);
                    row.put("phaseOrder", 0);
                    row.put("expectedDuration", null);
                    row.put("serviceId", null);
                    log.warn("TreatmentPhase not found for phaseId: {}", phaseStatus.getPhaseId());
                }
                
                return row;
            }).toList();
            result.put("tablePhases", tablePhases);
        } else {
            result.put("message", "No active treatment plan found");
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getPatientTreatmentHistory(UUID patientId) {
        log.info("Getting treatment history for patient: {}", patientId);
        List<TreatmentPlan> completedPlans = treatmentPlanRepository.findByPatientIdAndStatusOrderByEndDateDesc(patientId, "completed");
        
        // Format bảng cho FE
        List<Map<String, Object>> tableHistory = completedPlans.stream().map(plan -> {
            Map<String, Object> row = new HashMap<>();
            row.put("planId", plan.getPlanId());
            row.put("planName", plan.getPlanName());
            row.put("treatmentType", plan.getTreatmentType());
            row.put("startDate", plan.getStartDate());
            row.put("endDate", plan.getEndDate());
            row.put("status", plan.getStatus());
            row.put("doctorId", plan.getDoctorId());
            row.put("successProbability", plan.getSuccessProbability());
            row.put("estimatedCost", plan.getEstimatedCost());
            return row;
        }).toList();
        
        // Trả về cả danh sách gốc và bảng
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("history", completedPlans.stream().map(this::convertPlanToHistoryMap).toList());
        wrapper.put("tableHistory", tableHistory);
        result.add(wrapper);
        return result;
    }

    @Override
    public List<Map<String, Object>> getDoctorTreatmentWorkload(String doctorId) {
        log.info("Getting treatment workload for doctor: {}", doctorId);
        
        List<TreatmentPlan> activePlans = treatmentPlanRepository.findByDoctorIdOrderByCreatedDateDesc(UUID.fromString(doctorId));
        
        return activePlans.stream()
                .filter(plan -> "active".equals(plan.getStatus()))
                .map(this::convertPlanToWorkloadMap)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getPatientWorkflowStatus(UUID patientId) {
        log.info("Getting workflow status for patient: {}", patientId);
        
        Map<String, Object> status = new HashMap<>();
        
        // Check clinical results
        List<ClinicalResultResponse> clinicalResults = getPatientClinicalResults(patientId);
        status.put("hasClinicalResults", !clinicalResults.isEmpty());
        status.put("latestClinicalResult", clinicalResults.isEmpty() ? null : clinicalResults.get(0));
        
        // Check active treatment plan
        List<TreatmentPlan> activePlans = treatmentPlanRepository.findByStatusAndPatientId("active", patientId);
        status.put("hasActiveTreatmentPlan", !activePlans.isEmpty());
        status.put("activeTreatmentPlan", activePlans.isEmpty() ? null : activePlans.get(0));
        
        // Check treatment history
        List<TreatmentPlan> allPlans = treatmentPlanRepository.findByPatientIdOrderByCreatedDateDesc(patientId);
        status.put("totalTreatmentPlans", allPlans.size());
        status.put("treatmentHistory", allPlans);
        
        return status;
    }

    @Override
    public List<Map<String, Object>> getDoctorTreatmentPhases(UUID doctorId) {
        log.info("Getting all treatment phases for doctor: {}", doctorId);
        // Lấy tất cả plan active của bác sĩ
        List<TreatmentPlan> activePlans = treatmentPlanRepository.findByDoctorIdOrderByCreatedDateDesc(doctorId)
                .stream().filter(plan -> "active".equalsIgnoreCase(plan.getStatus())).toList();
        List<Map<String, Object>> tablePhases = new ArrayList<>();
        
        for (TreatmentPlan plan : activePlans) {
            List<TreatmentPhaseStatus> phases = treatmentPhaseStatusRepository.findByTreatmentPlanIdOrderByPhaseId(plan.getPlanId());
            for (TreatmentPhaseStatus phaseStatus : phases) {
                Map<String, Object> row = new HashMap<>();
                
                // FIX: Đổi tên field để tránh nhầm lẫn và JOIN để lấy tên thật
                row.put("phaseId", phaseStatus.getPhaseId()); // UUID của phase
                row.put("status", phaseStatus.getStatus());
                row.put("startDate", phaseStatus.getStartDate());
                row.put("endDate", phaseStatus.getEndDate());
                row.put("patientId", plan.getPatientId());
                row.put("planId", plan.getPlanId());
                
                // JOIN với TreatmentPhase để lấy thông tin thật
                TreatmentPhase treatmentPhase = treatmentPhaseRepository.findById(phaseStatus.getPhaseId()).orElse(null);
                if (treatmentPhase != null) {
                    row.put("phaseName", treatmentPhase.getPhaseName()); // Tên thật từ DB
                    row.put("description", treatmentPhase.getDescription());
                    row.put("phaseOrder", treatmentPhase.getPhaseOrder());
                    row.put("expectedDuration", treatmentPhase.getExpectedDuration());
                    row.put("serviceId", treatmentPhase.getServiceId());
                } else {
                    // Fallback nếu không tìm thấy phase
                    row.put("phaseName", "Unknown Phase");
                    row.put("description", null);
                    row.put("phaseOrder", 0);
                    row.put("expectedDuration", null);
                    row.put("serviceId", null);
                    log.warn("TreatmentPhase not found for phaseId: {}", phaseStatus.getPhaseId());
                }
                
                tablePhases.add(row);
            }
        }
        return tablePhases;
    }

    // ========== VALIDATION & BUSINESS RULES ==========

    @Override
    public boolean canCreateTreatmentPlan(UUID patientId) {
        // Cần có ít nhất 1 clinical result hoàn thành
        List<ClinicalResultResponse> results = getPatientClinicalResults(patientId);
        return results.stream().anyMatch(r -> Boolean.TRUE.equals(r.getIsCompleted()));
    }

    @Override
    public boolean canAdvanceToNextStep(UUID treatmentPlanId, Integer currentStep) {
        if (currentStep <= 0) return true; // Bước đầu tiên luôn được phép
        
        // Kiểm tra bước trước đó đã hoàn thành chưa
        List<TreatmentPhaseStatus> statuses = treatmentPhaseStatusRepository.findByTreatmentPlanIdOrderByPhaseId(treatmentPlanId);
        
        if (currentStep <= statuses.size()) {
            TreatmentPhaseStatus previousStepStatus = statuses.get(currentStep - 1);
            return "Completed".equals(previousStepStatus.getStatus());
        }
        
        return false;
    }

    @Override
    public boolean hasPatientAttendedSchedule(UUID treatmentPlanId, Integer stepOrder) {
        // Đơn giản hóa: chỉ kiểm tra phase status
        List<TreatmentPhaseStatus> phases = treatmentPhaseStatusRepository.findByTreatmentPlanIdOrderByPhaseId(treatmentPlanId);
        if (stepOrder <= phases.size()) {
            TreatmentPhaseStatus phase = phases.get(stepOrder - 1);
            return "Completed".equals(phase.getStatus());
        }
        return false;
    }

    // ========== EMAIL NOTIFICATIONS ==========

    @Override
    public void sendTreatmentPhasesEmail(UUID treatmentPlanId) {
        log.info("Sending treatment phases email for plan: {}", treatmentPlanId);
        
        try {
            TreatmentPlan plan = treatmentPlanRepository.findById(treatmentPlanId)
                    .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found: " + treatmentPlanId));
            
            // Lấy thông tin bệnh nhân và bác sĩ
            Optional<User> patientOpt = userRepository.findById(plan.getPatientId());
            Optional<User> doctorOpt = plan.getDoctorId() != null ? userRepository.findById(plan.getDoctorId()) : Optional.empty();
            
            if (patientOpt.isPresent()) {
                User patient = patientOpt.get();
                User doctor = doctorOpt.orElse(null);
                
                // Gửi email thông báo kế hoạch điều trị
                emailService.sendTreatmentPhasesEmail(patient, doctor, plan);
                log.info("Treatment phases email sent successfully for plan: {}", treatmentPlanId);
            } else {
                log.warn("Patient not found for treatment plan: {}", treatmentPlanId);
            }
        } catch (Exception e) {
            log.error("Failed to send treatment phases email for plan: {}", treatmentPlanId, e);
        }
    }

    @Override
    public void sendTreatmentCompletionEmail(UUID treatmentPlanId) {
        log.info("Sending treatment completion email for plan: {}", treatmentPlanId);
        
        try {
            TreatmentPlan plan = treatmentPlanRepository.findById(treatmentPlanId)
                    .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found: " + treatmentPlanId));
            
            // Lấy thông tin bệnh nhân và bác sĩ
            Optional<User> patientOpt = userRepository.findById(plan.getPatientId());
            Optional<User> doctorOpt = plan.getDoctorId() != null ? userRepository.findById(plan.getDoctorId()) : Optional.empty();
            
            if (patientOpt.isPresent()) {
                User patient = patientOpt.get();
                User doctor = doctorOpt.orElse(null);
                
                // Gửi email thông báo hoàn thành điều trị
                emailService.sendTreatmentCompletionEmail(patient, doctor, plan);
                log.info("Treatment completion email sent successfully for plan: {}", treatmentPlanId);
            } else {
                log.warn("Patient not found for treatment plan: {}", treatmentPlanId);
            }
        } catch (Exception e) {
            log.error("Failed to send treatment completion email for plan: {}", treatmentPlanId, e);
        }
    }

    @Override
    public void sendAppointmentReminderEmail(UUID treatmentPlanId, Integer stepOrder) {
        log.info("Sending appointment reminder email for plan: {}, step: {}", treatmentPlanId, stepOrder);
        
        try {
            // TODO: Implement appointment reminder email logic
            log.info("Appointment reminder email sent successfully for plan: {}, step: {}", treatmentPlanId, stepOrder);
        } catch (Exception e) {
            log.error("Failed to send appointment reminder email for plan: {}, step: {}", treatmentPlanId, stepOrder, e);
        }
    }

    // ========== PRIVATE HELPER METHODS ==========

    private TreatmentPlanRequest createRequestFromPreviousPlan(TreatmentPlanResponse previousPlan) {
        TreatmentPlanRequest request = new TreatmentPlanRequest();
        request.setTemplateId(previousPlan.getTemplateId());
        request.setTreatmentType(previousPlan.getTreatmentType());
        request.setPlanName("Điều trị lần 2 - " + previousPlan.getPlanName());
        request.setPlanDescription(previousPlan.getPlanDescription());
        // Copy other relevant fields
        return request;
    }

    private Map<String, Object> convertPlanToHistoryMap(TreatmentPlan plan) {
        Map<String, Object> map = new HashMap<>();
        map.put("planId", plan.getPlanId());
        map.put("planName", plan.getPlanName());
        map.put("treatmentType", plan.getTreatmentType());
        map.put("startDate", plan.getStartDate());
        map.put("endDate", plan.getEndDate());
        map.put("status", plan.getStatus());
        return map;
    }

    private Map<String, Object> convertPlanToWorkloadMap(TreatmentPlan plan) {
        Map<String, Object> map = new HashMap<>();
        map.put("planId", plan.getPlanId());
        map.put("patientId", plan.getPatientId());
        map.put("planName", plan.getPlanName());
        map.put("treatmentType", plan.getTreatmentType());
        map.put("startDate", plan.getStartDate());
        map.put("currentPhase", plan.getCurrentPhase());
        map.put("status", plan.getStatus());
        return map;
    }

    /**
     * Merge thông tin từ template vào request - có kiểm tra quyền bác sĩ
     */
    private void mergeTemplateIntoRequest(TreatmentPlanRequest request, String doctorId) {
        try {
            // Kiểm tra quyền bác sĩ trước khi merge template
            String doctorSpecialty = getDoctorSpecialty(doctorId);
            log.info("Doctor {} with specialty {} requesting template for treatment type: {}", 
                    doctorId, doctorSpecialty, request.getTreatmentType());
            
            // Kiểm tra bác sĩ có quyền tạo treatment plan cho loại này không
            if (!canDoctorAccessTemplate(doctorSpecialty, request.getTreatmentType())) {
                throw new IllegalStateException(
                    String.format("Bác sĩ có specialty '%s' không được phép tạo treatment plan cho loại '%s'. " +
                                "Chỉ bác sĩ IUI mới được tạo IUI, bác sĩ IVF mới được tạo IVF.", 
                                doctorSpecialty, request.getTreatmentType()));
            }
            
            TreatmentPlanTemplate template = treatmentPlanTemplateRepository
                    .findByTreatmentTypeIgnoreCaseAndIsActiveTrue(request.getTreatmentType());
            if (template != null) {
                request.setTemplateId(template.getTemplateId());
                if (request.getPlanName() == null || request.getPlanName().trim().isEmpty()) {
                    request.setPlanName(template.getPlanName());
                }
                if (request.getPlanDescription() == null || request.getPlanDescription().trim().isEmpty()) {
                    request.setPlanDescription(template.getPlanDescription());
                }
                if (request.getTreatmentSteps() == null) {
                    if (template.getTreatmentSteps() != null && !template.getTreatmentSteps().trim().isEmpty()) {
                        request.setTreatmentSteps(objectMapper.readValue(template.getTreatmentSteps(), new TypeReference<List<TreatmentStepDTO>>(){}));
                    }
                }
                if (request.getMedicationPlan() == null) {
                    if (template.getMedicationPlan() != null && !template.getMedicationPlan().trim().isEmpty()) {
                        request.setMedicationPlan(objectMapper.readValue(template.getMedicationPlan(), new TypeReference<List<MedicationPlanDTO>>(){}));
                    }
                }
                if (request.getMonitoringSchedule() == null) {
                    if (template.getMonitoringSchedule() != null && !template.getMonitoringSchedule().trim().isEmpty()) {
                        request.setMonitoringSchedule(objectMapper.readValue(template.getMonitoringSchedule(), new TypeReference<List<MonitoringScheduleDTO>>(){}));
                    }
                }
                if (request.getEstimatedDurationDays() == null) {
                    request.setEstimatedDurationDays(template.getEstimatedDurationDays());
                }
                if (request.getEstimatedCost() == null) {
                    request.setEstimatedCost(template.getEstimatedCost());
                }
                if (request.getSuccessProbability() == null) {
                    request.setSuccessProbability(template.getSuccessProbability());
                }
                if (request.getRiskFactors() == null || request.getRiskFactors().trim().isEmpty()) {
                    request.setRiskFactors(template.getRiskFactors());
                }
                if (request.getContraindications() == null || request.getContraindications().trim().isEmpty()) {
                    request.setContraindications(template.getContraindications());
                }
                // Copy đúng treatmentCycle từ template
                if (template.getTreatmentCycle() != null) {
                    request.setTreatmentCycle(template.getTreatmentCycle());
                }
                // Set status = 'active' khi merge template
                request.setStatus("active");
                log.info("Merged {} template '{}' into treatment plan request for doctor with specialty: {}", 
                        template.getTreatmentType(), template.getName(), doctorSpecialty);
            } else {
                log.warn("No active template found for treatment type: {}, using fallback", request.getTreatmentType());
                request.setTemplateId(UUID.fromString("B193D4C1-D9EE-41B4-A417-4121075BB91E"));
            }
        } catch (Exception e) {
            log.error("Error merging template into request: {}", e.getMessage());
            throw new RuntimeException("Không thể tạo treatment plan: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lấy specialty của bác sĩ từ profile
     */
    private String getDoctorSpecialty(String doctorId) {
        try {
            UUID doctorUuid = UUID.fromString(doctorId);
            User doctor = userRepository.findById(doctorUuid)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            
            return profileRepository.findByUser_Id(doctorUuid)
                    .map(profile -> profile.getSpecialty())
                    .orElse("UNKNOWN");
        } catch (Exception e) {
            log.error("Error getting doctor specialty: {}", e.getMessage());
            return "UNKNOWN";
        }
    }
    
    /**
     * Kiểm tra bác sĩ có quyền truy cập template không dựa trên specialty
     */
    private boolean canDoctorAccessTemplate(String doctorSpecialty, String treatmentType) {
        // Logic phân quyền:
        // - Bác sĩ IUI chỉ được truy cập template IUI
        // - Bác sĩ IVF chỉ được truy cập template IVF
        // - Bác sĩ có specialty khác hoặc UNKNOWN không được truy cập
        
        if (doctorSpecialty == null || doctorSpecialty.equals("UNKNOWN")) {
            return false;
        }
        
        String normalizedSpecialty = doctorSpecialty.toUpperCase().trim();
        String normalizedTreatmentType = treatmentType.toUpperCase().trim();
        
        // Mapping specialty với treatment type
        switch (normalizedSpecialty) {
            case "IUI":
                return "IUI".equals(normalizedTreatmentType);
            case "IVF":
                return "IVF".equals(normalizedTreatmentType);
            case "ICSI":
                return "ICSI".equals(normalizedTreatmentType);
            default:
                // Nếu specialty không khớp với treatment type nào, không cho phép
                return false;
        }
    }
    
    // ========== PHASE MANAGEMENT METHODS ==========
    
    @Override
    public PhaseStatusResponse updatePhaseStatus(UUID treatmentPlanId, UUID phaseId, PhaseStatusUpdateRequest request, String doctorId) {
        log.info("Updating phase status for plan: {}, phase: {} by doctor: {}", treatmentPlanId, phaseId, doctorId);
        
        // Kiểm tra quyền truy cập
        TreatmentPlan plan = treatmentPlanRepository.findById(treatmentPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found: " + treatmentPlanId));
        
        if (!plan.getDoctorId().equals(UUID.fromString(doctorId))) {
            throw new IllegalStateException("Only treating doctor can update phase status");
        }
        
        // Lấy phase status hiện tại
        TreatmentPhaseStatus phaseStatus = treatmentPhaseStatusRepository.findByTreatmentPlanIdAndPhaseId(treatmentPlanId, phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase status not found for plan: " + treatmentPlanId + ", phase: " + phaseId));
        
        // Lấy thông tin phase
        TreatmentPhase phase = treatmentPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase not found: " + phaseId));
        
        // Kiểm tra business rules
        validatePhaseStatusUpdate(phaseStatus, request.getStatus(), treatmentPlanId);
        
        // Cập nhật trạng thái
        String oldStatus = phaseStatus.getStatus();
        phaseStatus.setStatus(request.getStatus());
        phaseStatus.setNotes(request.getNotes());
        
        // Cập nhật thời gian
        if ("In Progress".equals(request.getStatus()) && phaseStatus.getStartDate() == null) {
            phaseStatus.setStartDate(LocalDateTime.now());
        } else if ("Completed".equals(request.getStatus())) {
            phaseStatus.setEndDate(LocalDateTime.now());
        }
        
        treatmentPhaseStatusRepository.save(phaseStatus);
        
        // Nếu hoàn thành phase, tự động mở phase tiếp theo
        if ("Completed".equals(request.getStatus())) {
            advanceToNextPhase(treatmentPlanId, phase.getPhaseOrder());
        }
        
        // Có thể thêm logic xử lý clinical result ở đây nếu cần
        
        log.info("Phase status updated from {} to {} for plan: {}, phase: {}", 
                oldStatus, request.getStatus(), treatmentPlanId, phaseId);
        
        return convertToPhaseStatusResponse(phaseStatus, phase);
    }
    
    @Override
    public List<PhaseStatusResponse> getTreatmentPlanPhases(UUID treatmentPlanId) {
        log.info("Getting all phases for treatment plan: {}", treatmentPlanId);
        
        List<TreatmentPhaseStatus> phaseStatuses = treatmentPhaseStatusRepository.findByTreatmentPlanIdOrderByPhaseId(treatmentPlanId);
        
        return phaseStatuses.stream()
                .map(phaseStatus -> {
                    TreatmentPhase phase = treatmentPhaseRepository.findById(phaseStatus.getPhaseId())
                            .orElse(null);
                    return convertToPhaseStatusResponse(phaseStatus, phase);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public PhaseStatusResponse getCurrentPhase(UUID treatmentPlanId) {
        log.info("Getting current phase for treatment plan: {}", treatmentPlanId);
        
        List<TreatmentPhaseStatus> phaseStatuses = treatmentPhaseStatusRepository.findByTreatmentPlanIdOrderByPhaseId(treatmentPlanId);
        
        // Tìm phase đang "In Progress"
        TreatmentPhaseStatus currentPhaseStatus = phaseStatuses.stream()
                .filter(ps -> "In Progress".equals(ps.getStatus()))
                .findFirst()
                .orElse(null);
        
        if (currentPhaseStatus == null) {
            // Nếu không có phase nào đang "In Progress", tìm phase đầu tiên "Pending"
            currentPhaseStatus = phaseStatuses.stream()
                    .filter(ps -> "Pending".equals(ps.getStatus()))
                    .findFirst()
                    .orElse(null);
        }
        
        if (currentPhaseStatus == null) {
            return null; // Không có phase nào đang thực hiện
        }
        
        TreatmentPhase phase = treatmentPhaseRepository.findById(currentPhaseStatus.getPhaseId())
                .orElse(null);
        
        return convertToPhaseStatusResponse(currentPhaseStatus, phase);
    }
    
    // ========== PRIVATE HELPER METHODS FOR PHASE MANAGEMENT ==========
    
    private void validatePhaseStatusUpdate(TreatmentPhaseStatus currentPhaseStatus, String newStatus, UUID treatmentPlanId) {
        // Kiểm tra trạng thái hợp lệ
        Set<String> validStatuses = Set.of("Pending", "In Progress", "Completed", "Cancelled");
        if (!validStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid status: " + newStatus + ". Valid statuses: " + validStatuses);
        }
        
        // Kiểm tra logic chuyển đổi trạng thái
        String currentStatus = currentPhaseStatus.getStatus();
        
        // Không cho phép chuyển từ Completed về trạng thái khác
        if ("Completed".equals(currentStatus) && !"Completed".equals(newStatus)) {
            throw new IllegalStateException("Cannot change status from Completed to " + newStatus);
        }
        
        // Không cho phép chuyển từ Cancelled về trạng thái khác
        if ("Cancelled".equals(currentStatus) && !"Cancelled".equals(newStatus)) {
            throw new IllegalStateException("Cannot change status from Cancelled to " + newStatus);
        }
        
        // Nếu muốn chuyển sang "In Progress", kiểm tra phase trước đã hoàn thành chưa
        if ("In Progress".equals(newStatus) && "Pending".equals(currentStatus)) {
            validateCanStartPhase(currentPhaseStatus, treatmentPlanId);
        }
    }
    
    private void validateCanStartPhase(TreatmentPhaseStatus phaseStatus, UUID treatmentPlanId) {
        // Lấy thông tin phase để biết thứ tự
        TreatmentPhase phase = treatmentPhaseRepository.findById(phaseStatus.getPhaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Phase not found: " + phaseStatus.getPhaseId()));
        
        // Nếu không phải phase đầu tiên, kiểm tra phase trước đã hoàn thành chưa
        if (phase.getPhaseOrder() > 1) {
            List<TreatmentPhaseStatus> allPhases = treatmentPhaseStatusRepository.findByTreatmentPlanIdOrderByPhaseId(treatmentPlanId);
            
            // Tìm phase trước đó
            TreatmentPhaseStatus previousPhaseStatus = allPhases.stream()
                    .filter(ps -> {
                        TreatmentPhase p = treatmentPhaseRepository.findById(ps.getPhaseId()).orElse(null);
                        return p != null && p.getPhaseOrder() == phase.getPhaseOrder() - 1;
                    })
                    .findFirst()
                    .orElse(null);
            
            if (previousPhaseStatus != null && !"Completed".equals(previousPhaseStatus.getStatus())) {
                throw new IllegalStateException("Cannot start phase " + phase.getPhaseOrder() + 
                        " because previous phase " + (phase.getPhaseOrder() - 1) + " is not completed");
            }
        }
    }
    
    private void advanceToNextPhase(UUID treatmentPlanId, Integer completedPhaseOrder) {
        log.info("Advancing to next phase after completing phase {} for plan: {}", completedPhaseOrder, treatmentPlanId);
        
        List<TreatmentPhaseStatus> allPhases = treatmentPhaseStatusRepository.findByTreatmentPlanIdOrderByPhaseId(treatmentPlanId);
        
        // Tìm phase tiếp theo
        TreatmentPhaseStatus nextPhaseStatus = allPhases.stream()
                .filter(ps -> {
                    TreatmentPhase phase = treatmentPhaseRepository.findById(ps.getPhaseId()).orElse(null);
                    return phase != null && phase.getPhaseOrder() == completedPhaseOrder + 1;
                })
                .findFirst()
                .orElse(null);
        
        if (nextPhaseStatus != null && "Pending".equals(nextPhaseStatus.getStatus())) {
            // Mở phase tiếp theo
            nextPhaseStatus.setStatus("In Progress");
            nextPhaseStatus.setStartDate(LocalDateTime.now());
            treatmentPhaseStatusRepository.save(nextPhaseStatus);
            
            // Cập nhật current_phase trong treatment plan
            TreatmentPlan plan = treatmentPlanRepository.findById(treatmentPlanId)
                    .orElseThrow(() -> new ResourceNotFoundException("Treatment plan not found: " + treatmentPlanId));
            plan.setCurrentPhase(nextPhaseStatus.getPhaseId());
            treatmentPlanRepository.save(plan);
            
            log.info("Advanced to next phase: {} for plan: {}", nextPhaseStatus.getPhaseId(), treatmentPlanId);
        } else {
            log.info("No next phase found or all phases completed for plan: {}", treatmentPlanId);
        }
    }
    
    private PhaseStatusResponse convertToPhaseStatusResponse(TreatmentPhaseStatus phaseStatus, TreatmentPhase phase) {
        PhaseStatusResponse response = new PhaseStatusResponse();
        response.setStatusId(phaseStatus.getStatusId());
        response.setTreatmentPlanId(phaseStatus.getTreatmentPlanId());
        response.setPhaseId(phaseStatus.getPhaseId());
        response.setStatus(phaseStatus.getStatus());
        response.setStartDate(phaseStatus.getStartDate());
        response.setEndDate(phaseStatus.getEndDate());
        response.setNotes(phaseStatus.getNotes());
        if (phase != null) {
            response.setPhaseName(phase.getPhaseName());
            response.setPhaseOrder(phase.getPhaseOrder());
            response.setDescription(phase.getDescription());
            response.setExpectedDuration(phase.getExpectedDuration());
        }
        // Không set clinicalResult vào response
        return response;
    }

    // ========== HELPER METHODS FROM TREATMENT PLAN SERVICE ==========

    private TreatmentPlanResponse convertToResponse(TreatmentPlan treatmentPlan) {
        TreatmentPlanResponse response = new TreatmentPlanResponse();
        org.springframework.beans.BeanUtils.copyProperties(treatmentPlan, response);
        // Ensure both ID fields are properly set from entity
        response.setPlanId(treatmentPlan.getPlanId());
        // Map doctorId (UUID) sang String cho DTO
        response.setDoctorId(treatmentPlan.getDoctorId() != null ? treatmentPlan.getDoctorId().toString() : null);
        
        // Chuyển đổi JSON string sang List cho FE
        try {
            if (treatmentPlan.getTreatmentSteps() != null && !treatmentPlan.getTreatmentSteps().trim().isEmpty()) {
                List<TreatmentStepDTO> treatmentSteps = objectMapper.readValue(
                    treatmentPlan.getTreatmentSteps(),
                    new TypeReference<List<TreatmentStepDTO>>() {}
                );
                response.setTreatmentSteps(treatmentSteps);
            }
            if (treatmentPlan.getMedicationPlan() != null && !treatmentPlan.getMedicationPlan().trim().isEmpty()) {
                List<MedicationPlanDTO> medicationPlan = objectMapper.readValue(
                    treatmentPlan.getMedicationPlan(),
                    new TypeReference<List<MedicationPlanDTO>>() {}
                );
                response.setMedicationPlan(medicationPlan);
            }
            if (treatmentPlan.getMonitoringSchedule() != null && !treatmentPlan.getMonitoringSchedule().trim().isEmpty()) {
                List<MonitoringScheduleDTO> monitoringSchedule = objectMapper.readValue(
                    treatmentPlan.getMonitoringSchedule(),
                    new TypeReference<List<MonitoringScheduleDTO>>() {}
                );
                response.setMonitoringSchedule(monitoringSchedule);
            }
        } catch (Exception e) {
            log.error("Error converting JSON string to List: {}", e.getMessage());
            // Không throw exception, chỉ log error để không ảnh hưởng đến response
        }
        
        return response;
    }

    /**
     * Auto-generate phases and phase status from treatment_steps JSON
     */
    private void createTreatmentPhasesFromPlan(TreatmentPlan plan) throws Exception {
        String treatmentStepsJson = plan.getTreatmentSteps();
        if (treatmentStepsJson == null || treatmentStepsJson.trim().isEmpty()) {
            log.warn("No treatment steps found for plan: {}", plan.getPlanId());
            return;
        }
        List<TreatmentStepDTO> treatmentSteps = objectMapper.readValue(
            treatmentStepsJson,
            new TypeReference<List<TreatmentStepDTO>>() {}
        );
        log.info("Parsed {} treatment steps for plan: {} (auto-generate phases)", treatmentSteps.size(), plan.getPlanId());
        UUID firstPhaseId = null;
        for (int i = 0; i < treatmentSteps.size(); i++) {
            TreatmentStepDTO step = treatmentSteps.get(i);
            TreatmentPhase phase = new TreatmentPhase();
            phase.setPhaseName(step.getName());
            phase.setPhaseOrder(step.getStep());
            phase.setDescription(step.getDescription());
            phase.setExpectedDuration(parseDurationToDays(step.getDuration()));
            // Lấy service ID dựa trên treatment type
            UUID serviceId = getServiceIdByTreatmentType(plan.getTreatmentType());
            phase.setServiceId(serviceId);
            TreatmentPhase savedPhase = treatmentPhaseRepository.save(phase);
            // Tạo TreatmentPhaseStatus
            TreatmentPhaseStatus phaseStatus = new TreatmentPhaseStatus();
            phaseStatus.setTreatmentPlanId(plan.getPlanId());
            phaseStatus.setPhaseId(savedPhase.getPhaseId());
            if (i == 0) {
                phaseStatus.setStatus("In Progress");
                phaseStatus.setStartDate(LocalDateTime.now());
                firstPhaseId = savedPhase.getPhaseId();
            } else {
                phaseStatus.setStatus("Pending");
            }
            treatmentPhaseStatusRepository.save(phaseStatus);
            log.info("Created phase: {} (order: {}) with status: {}", step.getName(), step.getStep(), phaseStatus.getStatus());
        }
        // Cập nhật current_phase trong TreatmentPlan
        if (firstPhaseId != null) {
            plan.setCurrentPhase(firstPhaseId);
            treatmentPlanRepository.save(plan);
            log.info("Updated current phase to: {} for plan: {}", firstPhaseId, plan.getPlanId());
        }
    }

    private Integer parseDurationToDays(String duration) {
        if (duration == null || duration.trim().isEmpty()) {
            return 1;
        }
        try {
            String cleanDuration = duration.replace("ngày", "").trim();
            if (cleanDuration.contains("-")) {
                String[] parts = cleanDuration.split("-");
                if (parts.length == 2) {
                    int start = Integer.parseInt(parts[0].trim());
                    int end = Integer.parseInt(parts[1].trim());
                    return (start + end) / 2;
                }
            }
            return Integer.parseInt(cleanDuration);
        } catch (NumberFormatException e) {
            log.warn("Could not parse duration: {}, using default 1 day", duration);
            return 1;
        }
    }

    private UUID getServiceIdByTreatmentType(String treatmentType) {
        if (treatmentType == null) {
            return UUID.fromString("82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1");
        }
        switch (treatmentType.toUpperCase()) {
            case "IUI":
                return UUID.fromString("82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1");
            case "IVF":
                return UUID.fromString("735d0713-6e9b-4ced-9621-a30190faebf3");
            case "ICSI":
                return UUID.fromString("735d0713-6e9b-4ced-9621-a30190faebf3");
            default:
                return UUID.fromString("82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1");
        }
    }
} 