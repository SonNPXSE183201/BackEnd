package com.ferticare.ferticareback.common.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingRequest {
    @Min(value = 0)
    private int page;
    @Min(value =1)
    private int size;

    private SortRequest sortRequest;

    public PagingRequest(int page, int size){
        this.page = page;
        this.size = size;
    }
}
