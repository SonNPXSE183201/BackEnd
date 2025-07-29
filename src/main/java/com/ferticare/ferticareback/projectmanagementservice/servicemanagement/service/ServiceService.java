package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceResponse;

import java.util.List;

public interface ServiceService {
    List<ServiceResponse> getAllServices();
} 