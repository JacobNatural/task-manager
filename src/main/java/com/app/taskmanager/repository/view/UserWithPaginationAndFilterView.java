package com.app.taskmanager.repository.view;

import com.app.taskmanager.repository.model.User;

import java.util.List;

/**
 * View DTO representing a paginated list of users.
 * <p>
 * Contains the list of users and related count information.
 */
public record UserWithPaginationAndFilterView(
        /**
         * List of users returned by the query.
         */
        List<User> elements,

        /**
         * List containing count information, typically the total count of users.
         */
        List<CountInfoView> countInfo
) {
}
