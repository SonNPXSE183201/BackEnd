package com.ferticare.ferticareback.common.utils;

import com.ferticare.ferticareback.common.constant.SortMessageConstant;
import com.ferticare.ferticareback.common.request.PagingRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

public class PagingUtil {
    public static Sort createSort(PagingRequest pagingRequest) {
        if (pagingRequest.getSortRequest() != null && StringUtils.hasText(pagingRequest.getSortRequest().getField())) {
            return Sort.by(
                    SortMessageConstant.SORT_DESC.equalsIgnoreCase(pagingRequest.getSortRequest().getDirection()) ?
                            Sort.Direction.DESC : Sort.Direction.ASC,
                    pagingRequest.getSortRequest().getField()
            );
        }
        return Sort.unsorted();
    }

    private PagingUtil() {
    }
}