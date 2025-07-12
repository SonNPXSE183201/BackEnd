package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.controller;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.DoctorOnly;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.CustomerOnly;
import com.ferticare.ferticareback.projectmanagementservice.configuration.security.auth.JwtUtil;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.ClinicalResultRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.TreatmentPlanRequest;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request.PhaseStatusUpdateRequest;

import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.ClinicalResultResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.TreatmentPlanResponse;
import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.response.PhaseStatusResponse;

import com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.service.TreatmentWorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/treatment-workflow")
@RequiredArgsConstructor
@Tag(name = "Treatment Workflow", description = "API quản lý workflow điều trị vô sinh")
public class TreatmentWorkflowController {

    private final TreatmentWorkflowService treatmentWorkflowService;
    private final JwtUtil jwtUtil;

    // ========== WORKFLOW CHÍNH - DOCTOR ENDPOINTS ==========

    @PostMapping("/treatment-plan/from-clinical-result/{resultId}")
    @DoctorOnly
<<<<<<< HEAD
    @Operation(summary = "4. Bác sĩ tạo phác đồ điều trị từ kết quả khám lâm sàng", 
               description = "Tự động tạo lịch điều trị và gửi email thông báo cho bệnh nhân")
=======
    @Operation(summary = "4. Bác sĩ tạo phác đồ điều trị từ kết quả khám lâm sàng",
            description = "Tự động tạo lịch điều trị và gửi email thông báo cho bệnh nhân")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TreatmentPlanResponse> createTreatmentPlanFromClinicalResult(
            @PathVariable String resultId,
            @Validated @RequestBody TreatmentPlanRequest request,
            HttpServletRequest httpRequest) {
<<<<<<< HEAD
        
        String jwt = jwtUtil.extractJwtFromRequest(httpRequest);
        String doctorId = jwtUtil.extractUserId(jwt);
        
=======

        String jwt = jwtUtil.extractJwtFromRequest(httpRequest);
        String doctorId = jwtUtil.extractUserId(jwt);

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        TreatmentPlanResponse response = treatmentWorkflowService.createTreatmentPlanFromClinicalResult(resultId, request, doctorId);
        return ResponseEntity.ok(response);
    }



    @PutMapping("/treatment-plan/{treatmentPlanId}/modify")
    @DoctorOnly
<<<<<<< HEAD
    @Operation(summary = "10. Bác sĩ thay đổi kế hoạch điều trị", 
               description = "Thay đổi phác đồ điều trị theo tình hình thực tế")
=======
    @Operation(summary = "10. Bác sĩ thay đổi kế hoạch điều trị",
            description = "Thay đổi phác đồ điều trị theo tình hình thực tế")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TreatmentPlanResponse> modifyTreatmentPlan(
            @PathVariable UUID treatmentPlanId,
            @Validated @RequestBody TreatmentPlanRequest modifications,
            HttpServletRequest httpRequest) {
<<<<<<< HEAD
        
        String jwt = jwtUtil.extractJwtFromRequest(httpRequest);
        String doctorId = jwtUtil.extractUserId(jwt);
        
=======

        String jwt = jwtUtil.extractJwtFromRequest(httpRequest);
        String doctorId = jwtUtil.extractUserId(jwt);

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        TreatmentPlanResponse response = treatmentWorkflowService.modifyTreatmentPlan(treatmentPlanId, modifications, doctorId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/treatment-plan/{treatmentPlanId}/complete")
    @DoctorOnly
<<<<<<< HEAD
    @Operation(summary = "11. Hoàn thành điều trị", 
               description = "Hoàn thành phác đồ điều trị và gửi email thông báo")
=======
    @Operation(summary = "11. Hoàn thành điều trị",
            description = "Hoàn thành phác đồ điều trị và gửi email thông báo")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> completeTreatmentPlan(
            @PathVariable UUID treatmentPlanId,
            @RequestParam(required = false) String notes,
            HttpServletRequest httpRequest) {
<<<<<<< HEAD
        
        String jwt = jwtUtil.extractJwtFromRequest(httpRequest);
        String doctorId = jwtUtil.extractUserId(jwt);
        
=======

        String jwt = jwtUtil.extractJwtFromRequest(httpRequest);
        String doctorId = jwtUtil.extractUserId(jwt);

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        treatmentWorkflowService.completeTreatmentPlan(treatmentPlanId, notes, doctorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/treatment-plan/{treatmentPlanId}/cancel")
    @DoctorOnly
<<<<<<< HEAD
    @Operation(summary = "12. Hủy kế hoạch điều trị", 
               description = "Hủy phác đồ điều trị do bệnh nhân không đến")
=======
    @Operation(summary = "12. Hủy kế hoạch điều trị",
            description = "Hủy phác đồ điều trị do bệnh nhân không đến")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> cancelTreatmentPlan(
            @PathVariable UUID treatmentPlanId,
            @RequestParam String reason,
            HttpServletRequest httpRequest) {
<<<<<<< HEAD
        
        String jwt = jwtUtil.extractJwtFromRequest(httpRequest);
        String doctorId = jwtUtil.extractUserId(jwt);
        
=======

        String jwt = jwtUtil.extractJwtFromRequest(httpRequest);
        String doctorId = jwtUtil.extractUserId(jwt);

>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
        treatmentWorkflowService.cancelTreatmentPlan(treatmentPlanId, reason, doctorId);
        return ResponseEntity.ok().build();
    }

    // ========== PATIENT ENDPOINTS ==========

    @GetMapping("/patient/{patientId}/clinical-results")
    @CustomerOnly
<<<<<<< HEAD
    @Operation(summary = "3. Bệnh nhân xem kết quả khám lâm sàng", 
               description = "Danh sách kết quả khám lâm sàng của bệnh nhân")
=======
    @Operation(summary = "3. Bệnh nhân xem kết quả khám lâm sàng",
            description = "Danh sách kết quả khám lâm sàng của bệnh nhân")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ClinicalResultResponse>> getPatientClinicalResults(@PathVariable UUID patientId) {
        List<ClinicalResultResponse> results = treatmentWorkflowService.getPatientClinicalResults(patientId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/patient/{patientId}/treatment-phases")
    @CustomerOnly
<<<<<<< HEAD
    @Operation(summary = "6. Bệnh nhân xem phases điều trị", 
               description = "Phases điều trị hiện tại của bệnh nhân")
=======
    @Operation(summary = "6. Bệnh nhân xem phases điều trị",
            description = "Phases điều trị hiện tại của bệnh nhân")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, Object>> getPatientTreatmentPhases(@PathVariable UUID patientId) {
        Map<String, Object> phases = treatmentWorkflowService.getPatientTreatmentPhases(patientId);
        return ResponseEntity.ok(phases);
    }

    @GetMapping("/patient/{patientId}/treatment-history")
    @CustomerOnly
<<<<<<< HEAD
    @Operation(summary = "14. Bệnh nhân xem lịch sử điều trị", 
               description = "Tất cả các phác đồ điều trị đã hoàn thành của bệnh nhân")
=======
    @Operation(summary = "14. Bệnh nhân xem lịch sử điều trị",
            description = "Tất cả các phác đồ điều trị đã hoàn thành của bệnh nhân")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Map<String, Object>>> getPatientTreatmentHistory(@PathVariable UUID patientId) {
        List<Map<String, Object>> history = treatmentWorkflowService.getPatientTreatmentHistory(patientId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/doctor/{doctorId}/treatment-phases")
    @DoctorOnly
    @Operation(summary = "Bác sĩ xem phases điều trị tổng hợp của các bệnh nhân mình điều trị", description = "Trả về danh sách phases điều trị dạng bảng cho dashboard bác sĩ")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Map<String, Object>>> getDoctorTreatmentPhases(@PathVariable UUID doctorId) {
        List<Map<String, Object>> phases = treatmentWorkflowService.getDoctorTreatmentPhases(doctorId);
        return ResponseEntity.ok(phases);
    }

    // ========== PHASE MANAGEMENT ENDPOINTS ==========

    @PutMapping("/treatment-plan/{treatmentPlanId}/phase/{phaseId}/status")
    @DoctorOnly
<<<<<<< HEAD
    @Operation(summary = "9. Bác sĩ cập nhật trạng thái phase điều trị", 
               description = "Cập nhật trạng thái phase và tự động chuyển sang phase tiếp theo khi hoàn thành")
=======
    @Operation(summary = "9. Bác sĩ cập nhật trạng thái phase điều trị",
            description = "Cập nhật trạng thái phase và tự động chuyển sang phase tiếp theo khi hoàn thành")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PhaseStatusResponse> updatePhaseStatus(
            @PathVariable UUID treatmentPlanId,
            @PathVariable UUID phaseId,
            @Validated @RequestBody PhaseStatusUpdateRequest request,
            HttpServletRequest httpRequest) {
        String jwt = jwtUtil.extractJwtFromRequest(httpRequest);
        String doctorId = jwtUtil.extractUserId(jwt);
        PhaseStatusResponse response = treatmentWorkflowService.updatePhaseStatus(treatmentPlanId, phaseId, request, doctorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/treatment-plan/{treatmentPlanId}/phases")
<<<<<<< HEAD
    @Operation(summary = "Lấy danh sách tất cả phases của treatment plan", 
               description = "Trả về danh sách phases với trạng thái hiện tại")
=======
    @Operation(summary = "Lấy danh sách tất cả phases của treatment plan",
            description = "Trả về danh sách phases với trạng thái hiện tại")
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<PhaseStatusResponse>> getTreatmentPlanPhases(@PathVariable UUID treatmentPlanId) {
        List<PhaseStatusResponse> phases = treatmentWorkflowService.getTreatmentPlanPhases(treatmentPlanId);
        return ResponseEntity.ok(phases);
    }

    @GetMapping("/treatment-plan/{treatmentPlanId}/current-phase")
<<<<<<< HEAD
    @Operation(summary = "Lấy phase hiện tại đang thực hiện", 
               description = "Trả về phase đang 'In Progress' hoặc phase đầu tiên 'Pending'")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getCurrentPhase(@PathVariable UUID treatmentPlanId) {
        PhaseStatusResponse currentPhase = treatmentWorkflowService.getCurrentPhase(treatmentPlanId);
        if (currentPhase == null) {
            return ResponseEntity.ok(new com.ferticare.ferticareback.common.dto.MessageDTO(
                "NO_CURRENT_PHASE",
                "Không có phase hiện tại"
            ));
        }
        return ResponseEntity.ok(currentPhase);
    }
} 
=======
    @Operation(summary = "Lấy phase hiện tại đang thực hiện",
            description = "Trả về phase đang 'In Progress' hoặc phase đầu tiên 'Pending'")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PhaseStatusResponse> getCurrentPhase(@PathVariable UUID treatmentPlanId) {
        PhaseStatusResponse currentPhase = treatmentWorkflowService.getCurrentPhase(treatmentPlanId);
        return ResponseEntity.ok(currentPhase);
    }
}
>>>>>>> 1e5b47cf8f4df1302b4cc5c648ae9c9a3e6a4f43
