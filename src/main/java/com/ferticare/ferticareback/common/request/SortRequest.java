package com.ferticare.ferticareback.common.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortRequest {
    @NotNull
    private String direction;
    @NotNull
    private String field;
}
