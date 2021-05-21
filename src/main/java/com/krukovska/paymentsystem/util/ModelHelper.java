package com.krukovska.paymentsystem.util;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.krukovska.paymentsystem.util.Constants.*;

public class ModelHelper {

    private ModelHelper() {
    }

    public static void setSortingPaginationAttributes(Model model,
                                                      Optional<Integer> pageNumber,
                                                      Optional<String> sortField,
                                                      Optional<String> sortDir,
                                                      Page<?> page) {
        model.addAttribute("sortDir", sortDir.orElse(DEFAULT_SORTING_ORDER));
        model.addAttribute("reverseSortDir", sortDir.map(s -> "asc".equals(s) ? "desc" : "asc")
                .orElse(DEFAULT_REVERSE_SORTING_ORDER));
        model.addAttribute("sortField", sortField.orElse(DEFAULT_SORTING_FIELD));
        setPaginationAttributes(model, pageNumber, page);
    }

    public static void setPaginationAttributes(Model model, Optional<Integer> pageNumber, Page<?> page) {
        model.addAttribute("page", pageNumber.orElse(DEFAULT_CURRENT_PAGE));

        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            model.addAttribute("pageNumbers", IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList()));
        }
    }
}
