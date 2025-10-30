package com.app.taskmanager.repository;

import com.app.taskmanager.repository.model.Task;
import com.app.taskmanager.repository.view.TaskWithPaginationAndFilterView;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Repository interface for {@link Task} entities.
 * <p>
 * Extends {@link ReactiveMongoRepository} for standard CRUD operations
 * and {@link CustomTaskRepository} for custom queries and updates.
 */
public interface TaskRepository extends ReactiveMongoRepository<Task, String>, CustomGenericFilter<TaskWithPaginationAndFilterView>, CustomTaskRepository {
}
