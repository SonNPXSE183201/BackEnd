package com.ferticare.ferticareback.projectmanagementservice.notificationmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEmailDTO {
    private String customerName;
    private String customerEmail;
    private String doctorName;
    private String serviceName;
    private String appointmentDate;
    private String appointmentTime;
    private String room;
    private String notes;
    private String status;
} 