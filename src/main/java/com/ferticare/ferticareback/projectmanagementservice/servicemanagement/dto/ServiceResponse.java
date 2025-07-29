package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ServiceResponse {
    private UUID id;
    private String name;
    private String description;
}