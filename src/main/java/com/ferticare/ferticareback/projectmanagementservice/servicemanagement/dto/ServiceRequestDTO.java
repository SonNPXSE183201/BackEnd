package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ServiceRequestDTO {
    private UUID serviceId;
    private boolean manualSelect;
    private UUID doctorId;
//    private LocalDateTime appointmentTime;
    private String note;
    private String doctorSelection; // "Manual" hoặc "Auto"
    private UUID preferredDoctorId;
    private String email;
    private String phone;
    private String address;
    private String idNumber;
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentTime;
}