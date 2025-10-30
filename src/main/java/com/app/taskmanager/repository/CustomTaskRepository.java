package com.app.taskmanager.repository;

import com.mongodb.client.result.UpdateResult;
import reactor.core.publisher.Mono;

/**
 * Custom repository interface for advanced task queries and operations.
 * <p>
 * Provides methods for paginated and filtered task retrieval as well as updating task assignments.
 */
public interface CustomTaskRepository {

    /**
     * Unassigns all tasks currently assigned to a specific user.
     *
     * @param userId the ID of the user whose tasks will be unassigned
     * @return a {@link Mono} emitting an {@link UpdateResult} with the update result
     */
    Mono<UpdateResult> unassignUserTasks(String userId);

    /**
     * Unassigns a specific task from a specific user.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task to unassign
     * @return a {@link Mono} emitting an {@link UpdateResult} with the update result
     */
    Mono<UpdateResult> unassignUserTask(String userId, String taskId);
}
