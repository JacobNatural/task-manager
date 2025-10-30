package com.app.taskmanager.repository.impl;

import com.app.taskmanager.repository.generic.CustomGenericFilter;
import com.app.taskmanager.repository.view.UserWithPaginationAndFilterView;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for users with pagination and filtering support.
 * <p>
 * Extends the {@code CustomGenericFilter} to provide MongoDB-based
 * pagination and filtering functionality for user entities.
 */
@Repository
public class UserRepositoryImpl extends CustomGenericFilter<UserWithPaginationAndFilterView> {

    /**
     * Constructs a new {@link UserRepositoryImpl} with the provided {@link ReactiveMongoTemplate}.
     *
     * @param mongoTemplate the reactive MongoDB template used for executing queries
     */
    public UserRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        super(mongoTemplate, "users");
    }
}
