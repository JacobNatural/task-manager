package com.app.taskmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Generic response DTO representing a paginated list of items.
 *
 * @param <T> the type of items in the page
 */
public record PageResponseDto<T>(
        /**
         * List of items in the current page.
         */
        List<T> list,

        /**
         * Total number of items across all pages.
         */

        long total,

        /**
         * Current page number (zero-based).
         */

        long page,

        /**
         * Number of items per page.
         */

        long size
) {
}
