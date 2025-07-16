package com.ferticare.ferticareback.projectmanagementservice.treatmentmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PhaseStatusUpdateRequest {

    @NotNull(message = "Status is required")
    @NotBlank(message = "Status cannot be blank")
    private String status; // 'Pending', 'In Progress', 'Completed', 'Cancelled'

    private String notes;
}