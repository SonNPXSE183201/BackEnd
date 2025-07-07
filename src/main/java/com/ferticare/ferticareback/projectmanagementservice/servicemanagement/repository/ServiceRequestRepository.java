package com.ferticare.ferticareback.projectmanagementservice.servicemanagement.repository;

import com.ferticare.ferticareback.projectmanagementservice.servicemanagement.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.UUID;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, UUID> {

    /**
     * Kiểm tra xem đã có request trùng lặp chưa (cùng customer, service, thời gian)
     */
    boolean existsByCustomerIdAndServiceIdAndPreferredDatetime(UUID customerId, UUID serviceId, LocalDateTime preferredDatetime);
}
