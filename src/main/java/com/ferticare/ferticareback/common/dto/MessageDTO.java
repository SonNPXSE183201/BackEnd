package com.ferticare.ferticareback.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MessageDTO {
    private String messageCode;
    private String messageDetail;
}
