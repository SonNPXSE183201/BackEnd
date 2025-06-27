package com.ferticare.ferticareback.projectmanagementservice.profile.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAddressRequest {
    private String address;
}

