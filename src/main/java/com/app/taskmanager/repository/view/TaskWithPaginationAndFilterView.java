package com.app.taskmanager.repository.view;

import com.app.taskmanager.repository.model.Task;
import java.util.List;

/**
 * View DTO representing a paginated and optionally filtered list of tasks.
 * <p>
 * Contains the list of tasks and related count information.
 */
public record TaskWithPaginationAndFilterView(
        /**
         * List of tasks returned by the query.
         */
        List<Task> tasks,

        /**
         * List containing count information, typically total count of tasks matching the filter.
         */
        List<CountInfoView> countInfo
) {
}
