package com.ferticare.ferticareback.projectmanagementservice.usermanagement.controller.manager;

import com.ferticare.ferticareback.projectmanagementservice.configuration.security.annotation.ManagerOnly;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.request.DoctorScheduleDTO;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class ManagerDoctorController {

    private final UserService userService;

    public ManagerDoctorController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Lấy danh sách tất cả bác sĩ và lịch làm việc 
     * Chỉ Manager mới có quyền xem để quản lý
     */
    @ManagerOnly
    @GetMapping
    public ResponseEntity<List<DoctorScheduleDTO>> getDoctors() {
        List<DoctorScheduleDTO> result = userService.getDoctorsWithSchedule();
        return ResponseEntity.ok(result);
    }
}