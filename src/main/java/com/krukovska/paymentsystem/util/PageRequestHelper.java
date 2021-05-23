package com.krukovska.paymentsystem.util;

import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.DEFAULT_CURRENT_PAGE;
import static com.krukovska.paymentsystem.util.Constants.DEFAULT_PAGE_SIZE;

public class PageRequestHelper {
    private PageRequestHelper() {
    }


    public static PageRequest createPageRequest(Optional<Integer> pageNumber, Optional<Integer> pageSize,
                                                Optional<String> sortField, Optional<String> sortDirection) {
        return PageRequest.of(
                pageNumber.orElse(DEFAULT_CURRENT_PAGE) - 1,
                pageSize.orElse(DEFAULT_PAGE_SIZE),
                SortHelper.buildSort(sortField, sortDirection));
    }
}
