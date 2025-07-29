package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.impl;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.dto.ServiceResponse;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository.ServiceRepository;
import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Override
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(s -> new ServiceResponse(s.getServiceId(), s.getName(), s.getDescription()))
                .toList();
    }
} 