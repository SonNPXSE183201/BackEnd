package com.ferticare.ferticareback.projectmanagementservice.usermanagement.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDoctorResponse {
    private UUID id;
    private String fullName;
    @Column(name = "work_schedule") // ✅ CHUẨN
    private String workSchedule;
}