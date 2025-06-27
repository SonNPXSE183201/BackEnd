package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ServiceRequestDTO {
    private UUID serviceId;
    private UUID doctorId;
    private String note;
    private String doctorSelection; // "Manual" hoặc "Auto"
    private LocalDateTime appointmentTime;
}