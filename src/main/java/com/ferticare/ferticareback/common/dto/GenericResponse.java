package com.ferticare.ferticareback.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse<T> {
    private boolean isSuccess = true;
    private MessageDTO message;
    private List<ErrorDTO> errors;
    private T data;
}
