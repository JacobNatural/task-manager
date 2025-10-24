package com.app.taskmanager.repository.view;

/**
 * View DTO representing a count of items.
 * <p>
 * Typically used to return the total number of records in a collection or query result.
 */
public record CountInfoView(
        /**
         * Total number of items.
         */
        Long totalCount
) {
}
