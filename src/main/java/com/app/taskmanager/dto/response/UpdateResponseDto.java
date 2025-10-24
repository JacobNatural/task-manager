package com.app.taskmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO representing the result of an update operation.
 * <p>
 * Contains the number of matched records and the number of successfully modified records.
 */
public record UpdateResponseDto(
        /**
         * Number of records that matched the update criteria.
         */

        long matched,

        /**
         * Number of records that were successfully modified.
         */

        long modified
) {
}
