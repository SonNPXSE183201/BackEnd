package com.ferticare.ferticareback.projectmanagementservice.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsDTO {
    private long total;
    private long admin;
    private long manager;
    private long doctor;
    private long patient;
    private long customer;
    private long active;
    private long inactive;
} 