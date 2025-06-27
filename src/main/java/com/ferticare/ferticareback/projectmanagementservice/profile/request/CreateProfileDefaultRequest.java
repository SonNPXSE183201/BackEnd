package com.ferticare.ferticareback.projectmanagementservice.profile.request;

import lombok.Data;

@Data
public class CreateProfileDefaultRequest {
    private String fullName;
    private String gender;
    private String phone;
    private String address;
}