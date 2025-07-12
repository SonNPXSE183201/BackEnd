package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.ClinicalResultRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.TreatmentPlanRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.PhaseStatusUpdateRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.ClinicalResultResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.TreatmentPlanResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.PhaseStatusResponse;


import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service điều phối workflow quản lý điều trị vô sinh
<<<<<<< HEAD
 * 
=======
 *
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
 * WORKFLOW LOGIC:
 * 1. Bệnh nhân đăng ký → có lịch khám lâm sàng (Appointment → PatientVisit)
 * 2. Bệnh nhân đến khám → bác sĩ cập nhật kết quả khám lâm sàng (ClinicalResult)
 * 3. Bệnh nhân có thể xem kết quả khám lâm sàng của mình
 * 4. Bác sĩ dựa vào kết quả khám lâm sàng tạo phác đồ điều trị từ template
 * 5. Sau khi tạo phác đồ → tự động tạo phases điều trị
 * 6. Hệ thống gửi email lịch điều trị cho bệnh nhân
 * 7. Bệnh nhân đến khám theo lịch điều trị
 * 8. Mỗi bước hoàn thành thì mới tiến tới bước tiếp theo (sequential)
 * 9. Bác sĩ cập nhật kết quả từng bước điều trị
 * 10. Bác sĩ có thể thay đổi kế hoạch điều trị theo tình hình thực tế
 * 11. Sau khi điều trị kết thúc → gửi email thông báo hoàn thành
 * 12. Nếu bệnh nhân không đến theo lịch → hủy kế hoạch
 * 13. Nếu muốn điều trị lại → bác sĩ xem lại kết quả cũ và tạo phác đồ mới
 * 14. Bệnh nhân có thể xem lịch sử điều trị
 */
public interface TreatmentWorkflowService {

    // ========== CRUD OPERATIONS (from TreatmentPlanService) ==========
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Tạo treatment plan cơ bản
     */
    TreatmentPlanResponse createTreatmentPlan(TreatmentPlanRequest request);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Tạo treatment plan với doctor ID
     */
    TreatmentPlanResponse createTreatmentPlanWithDoctor(TreatmentPlanRequest request, String doctorId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Lấy treatment plan theo ID
     */
    TreatmentPlanResponse getTreatmentPlanById(UUID planId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Cập nhật treatment plan
     */
    TreatmentPlanResponse updateTreatmentPlan(UUID planId, TreatmentPlanRequest request);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Xóa treatment plan
     */
    void deleteTreatmentPlan(UUID planId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Lấy danh sách treatment plans theo patient
     */
    List<TreatmentPlanResponse> getTreatmentPlansByPatient(UUID patientId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Lấy danh sách treatment plans theo doctor
     */
    List<TreatmentPlanResponse> getTreatmentPlansByDoctor(String doctorId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Lấy danh sách treatment plans theo type
     */
    List<TreatmentPlanResponse> getTreatmentPlansByType(String treatmentType);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Kích hoạt treatment plan (approve)
     */
    TreatmentPlanResponse approveTreatmentPlan(UUID planId, String approverId, String notes);

    // ========== WORKFLOW CHÍNH ==========
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 4. Bác sĩ tạo phác đồ điều trị từ kết quả khám lâm sàng
     * - Tự động tạo lịch điều trị từ template
     * - Gửi email thông báo lịch điều trị
     */
    TreatmentPlanResponse createTreatmentPlanFromClinicalResult(String clinicalResultId, TreatmentPlanRequest request, String doctorId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 9. Bác sĩ cập nhật trạng thái phase điều trị
     * Logic: Chỉ cho phép cập nhật phase hiện tại, hoàn thành thì mở phase tiếp theo
     */
    PhaseStatusResponse updatePhaseStatus(UUID treatmentPlanId, UUID phaseId, PhaseStatusUpdateRequest request, String doctorId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Lấy danh sách phases của treatment plan
     */
    List<PhaseStatusResponse> getTreatmentPlanPhases(UUID treatmentPlanId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Lấy phase hiện tại đang thực hiện
     */
    PhaseStatusResponse getCurrentPhase(UUID treatmentPlanId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 10. Bác sĩ thay đổi kế hoạch điều trị
     */
    TreatmentPlanResponse modifyTreatmentPlan(UUID treatmentPlanId, TreatmentPlanRequest modifications, String doctorId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 11. Hoàn thành điều trị (tự động gửi email)
     * Note: finalOutcome được lưu trong treatment_result thay vì treatment_plan
     */
    void completeTreatmentPlan(UUID treatmentPlanId, String notes, String doctorId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 12. Hủy kế hoạch điều trị (do bệnh nhân không đến)
     */
    void cancelTreatmentPlan(UUID treatmentPlanId, String reason, String doctorId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 13. Tạo phác đồ mới dựa trên lịch sử điều trị cũ
     */
    TreatmentPlanResponse recreateTreatmentPlanFromHistory(UUID patientId, UUID previousTreatmentPlanId, String doctorId);
<<<<<<< HEAD
    
    // ========== QUERY OPERATIONS ==========
    
=======

    // ========== QUERY OPERATIONS ==========

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 3. Bệnh nhân xem kết quả khám lâm sàng của mình
     */
    List<ClinicalResultResponse> getPatientClinicalResults(UUID patientId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 6. Bệnh nhân xem phases điều trị của mình
     */
    Map<String, Object> getPatientTreatmentPhases(UUID patientId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 14. Bệnh nhân xem lịch sử điều trị
     */
    List<Map<String, Object>> getPatientTreatmentHistory(UUID patientId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Bác sĩ xem danh sách bệnh nhân cần điều trị
     */
    List<Map<String, Object>> getDoctorTreatmentWorkload(String doctorId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Kiểm tra trạng thái workflow hiện tại của bệnh nhân
     */
    Map<String, Object> getPatientWorkflowStatus(UUID patientId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Bác sĩ xem danh sách phases điều trị của mình
     */
    List<Map<String, Object>> getDoctorTreatmentPhases(UUID doctorId);
<<<<<<< HEAD
    
    // ========== VALIDATION & BUSINESS RULES ==========
    
=======

    // ========== VALIDATION & BUSINESS RULES ==========

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Kiểm tra có thể tạo phác đồ điều trị không (cần có kết quả khám lâm sàng)
     */
    boolean canCreateTreatmentPlan(UUID patientId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Kiểm tra có thể tiến tới bước tiếp theo không (bước hiện tại đã hoàn thành)
     */
    boolean canAdvanceToNextStep(UUID treatmentPlanId, Integer currentStep);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Kiểm tra bệnh nhân có đến khám theo lịch không
     */
    boolean hasPatientAttendedSchedule(UUID treatmentPlanId, Integer stepOrder);
<<<<<<< HEAD
    
    // ========== EMAIL NOTIFICATIONS ==========
    
=======

    // ========== EMAIL NOTIFICATIONS ==========

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 6. Gửi email thông báo phases điều trị cho bệnh nhân
     */
    void sendTreatmentPhasesEmail(UUID treatmentPlanId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * 11. Gửi email thông báo hoàn thành điều trị
     */
    void sendTreatmentCompletionEmail(UUID treatmentPlanId);
<<<<<<< HEAD
    
=======

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    /**
     * Gửi email nhắc nhở lịch hẹn
     */
    void sendAppointmentReminderEmail(UUID treatmentPlanId, Integer stepOrder);
} 