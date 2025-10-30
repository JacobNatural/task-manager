package com.app.taskmanager.service;

import com.app.taskmanager.dto.create.CreateTaskDto;
import com.app.taskmanager.dto.create.UpdateTaskDto;
import com.app.taskmanager.dto.filters.FilterDto;
import com.app.taskmanager.dto.response.IdResponseDto;
import com.app.taskmanager.dto.response.PageResponseDto;
import com.app.taskmanager.dto.response.TaskResponseDto;
import com.app.taskmanager.dto.response.UpdateResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service interface for managing tasks.
 * <p>
 * Defines methods for creating, retrieving, updating, assigning, completing,
 * unassigning, and deleting tasks.
 */
public interface TaskService {

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task
     * @return a {@link Mono} emitting the {@link TaskResponseDto} if found, or an error if not found
     */
    Mono<TaskResponseDto> findTaskByID(String id);

    /**
     * Retrieves all tasks with optional filtering and pagination.
     *
     * @param page   the page number (zero-based)
     * @param size   the number of tasks per page
     * @param filter the {@link FilterDto} containing filtering criteria
     * @return a {@link Mono} emitting a {@link PageResponseDto} of {@link TaskResponseDto}
     */
    Mono<PageResponseDto<TaskResponseDto>> findAllTasks(
            long page, long size, FilterDto filter);

    /**
     * Creates a new task.
     *
     * @param task the {@link CreateTaskDto} containing task information
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the created task
     */
    Mono<IdResponseDto> createTask(CreateTaskDto task);

    /**
     * Updates an existing task.
     *
     * @param id   the ID of the task to update
     * @param task the {@link UpdateTaskDto} containing updated information
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the updated task
     */
    Mono<IdResponseDto> updateTask(String id, UpdateTaskDto task);

    /**
     * Unassigns all tasks from a user.
     *
     * @param userId the ID of the user
     * @return a {@link Mono} emitting an {@link UpdateResponseDto} with matched and modified counts
     */
    Mono<UpdateResponseDto> unassignUserTasks(String userId);

    /**
     * Unassigns a specific task from a user.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task
     * @return a {@link Mono} emitting an {@link UpdateResponseDto} with matched and modified counts
     */
    Mono<UpdateResponseDto> unassignUserTask(String userId, String taskId);

    /**
     * Assigns a list of tasks to a user.
     *
     * @param userId the ID of the user
     * @param taskId the list of task IDs to assign
     * @return a {@link Mono} emitting a list of {@link IdResponseDto} for assigned tasks
     */
    Mono<List<IdResponseDto>> assignUserTasks(String userId, List<String> taskId);

    /**
     * Marks a task as completed for a specific user.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the completed task
     */
    Mono<IdResponseDto> completeTask(String userId, String taskId);

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the deleted task,
     *         or an error if the task does not exist
     */
    Mono<IdResponseDto> deleteTask(String id);
}
