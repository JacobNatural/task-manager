package com.app.taskmanager.repository;

import com.app.taskmanager.repository.view.TaskWithPaginationAndFilterView;
import com.app.taskmanager.repository.model.Status;
import com.mongodb.client.result.UpdateResult;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

/**
 * Custom repository interface for advanced task queries and operations.
 * <p>
 * Provides methods for paginated and filtered task retrieval as well as updating task assignments.
 */
public interface CustomTaskRepository {

    /**
     * Retrieves tasks with pagination and optional filtering by user, status, and date range.
     *
     * @param size    the number of tasks per page
     * @param page    the page number (zero-based)
     * @param userId  optional ID of the user to filter tasks by
     * @param status  optional status to filter tasks by
     * @param fromTime optional lower bound for task creation date
     * @param toTime   optional upper bound for task creation date
     * @return a {@link Mono} emitting a {@link TaskWithPaginationAndFilterView} containing tasks and count information
     */
    Mono<TaskWithPaginationAndFilterView> findTaskWithPaginationAndFilter(
            long size, long page, String userId, Status status, LocalDateTime fromTime, LocalDateTime toTime);

    /**
     * Unassigns all tasks currently assigned to a specific user.
     *
     * @param userId the ID of the user whose tasks will be unassigned
     * @return a {@link Mono} emitting an {@link com.mongodb.client.result.UpdateResult} with the update result
     */
    Mono<UpdateResult> unassignUserTasks(String userId);

    /**
     * Unassigns a specific task from a specific user.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task to unassign
     * @return a {@link Mono} emitting an {@link com.mongodb.client.result.UpdateResult} with the update result
     */
    Mono<UpdateResult> unassignUserTask(String userId, String taskId);
}
