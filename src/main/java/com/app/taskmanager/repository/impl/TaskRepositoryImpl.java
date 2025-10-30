package com.app.taskmanager.repository.impl;

import com.app.taskmanager.repository.generic.CustomGenericFilter;
import com.app.taskmanager.repository.view.TaskWithPaginationAndFilterView;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for tasks with pagination and filtering support.
 * <p>
 * Extends the {@code CustomGenericFilter} to provide MongoDB-based
 * pagination and filtering functionality for task entities.
 */
@Repository
public class TaskRepositoryImpl extends CustomGenericFilter<TaskWithPaginationAndFilterView> {

    /**
     * Constructs a new {@link TaskRepositoryImpl} with the provided {@link ReactiveMongoTemplate}.
     *
     * @param mongoTemplate the reactive MongoDB template used for executing queries
     */
    public TaskRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        super(mongoTemplate, "tasks");
    }
}
