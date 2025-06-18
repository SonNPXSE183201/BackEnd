package com.ferticare.ferticareback.projectmanagementservice.usermanagement.request;

import com.ferticare.ferticareback.common.annotation.TrimmedNotBlank;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.Gender;
import com.ferticare.ferticareback.projectmanagementservice.usermanagement.enumeration.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

import static com.ferticare.ferticareback.common.constant.BaseFieldConstant.LARGE_STRING_LENGTH;
import static com.ferticare.ferticareback.common.constant.BaseFieldConstant.MEDIUM_STRING_LENGTH;
import static com.ferticare.ferticareback.common.constant.DataPatternConstant.*;
import static com.ferticare.ferticareback.common.constant.MessageConstant.*;

@Data
public class UserCreateByAdminRequest {

    @TrimmedNotBlank(message = REQUIRED_FIELD)
    @Size(max = MEDIUM_STRING_LENGTH, message = "Họ tên tối đa " + MEDIUM_STRING_LENGTH + " ký tự")
    private String fullName;

    private Gender gender;

    @NotNull(message = REQUIRED_FIELD)
    private LocalDate dateOfBirth;

    @TrimmedNotBlank(message = REQUIRED_FIELD)
    @Email(regexp = EMAIL_PATTERN, message = INVALID_EMAIL)
    private String email;

    @TrimmedNotBlank(message = REQUIRED_FIELD)
    @Pattern(regexp = PHONE_PATTERN, message = INVALID_PHONE)
    private String phone;

    @TrimmedNotBlank(message = REQUIRED_FIELD)
    @Size(max = LARGE_STRING_LENGTH, message = "Địa chỉ tối đa " + LARGE_STRING_LENGTH + " ký tự")
    private String address;

    private String avatarUrl;

    @TrimmedNotBlank(message = REQUIRED_FIELD)
    @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_TOO_SHORT)
    private String password;

    @NotNull(message = REQUIRED_FIELD)
    private RoleType role;
}