package com.app.taskmanager.service;

import com.app.taskmanager.dto.create.AddTaskDto;
import com.app.taskmanager.dto.create.CreateUserDto;
import com.app.taskmanager.dto.response.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service interface for managing users.
 * <p>
 * Defines methods for creating, retrieving, updating, and deleting users,
 * as well as assigning, completing, and unassigning tasks for users.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user the {@link CreateUserDto} containing user information
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the created user
     */
    Mono<IdResponseDto> createUser(CreateUserDto user);

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return a {@link Mono} emitting the {@link UserResponseDto} if found, or an error if not found
     */
    Mono<UserResponseDto> findUserByID(String id);

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user
     * @return a {@link Mono} emitting the {@link UserResponseDto} if found, or an error if not found
     */
    Mono<UserResponseDto> findUserByUsername(String username);

    /**
     * Retrieves all users with pagination.
     *
     * @param page the page number (zero-based)
     * @param size the number of users per page
     * @return a {@link Mono} emitting a {@link PageResponseDto} of {@link UserResponseDto}
     */
    Mono<PageResponseDto<UserResponseDto>> findAllUsers(int page, int size);

    /**
     * Assigns tasks to a user.
     *
     * @param userId     the ID of the user
     * @param addTaskDto the {@link AddTaskDto} containing task IDs to assign
     * @return a {@link Mono} emitting a list of {@link IdResponseDto} for assigned tasks
     */
    Mono<List<IdResponseDto>> addTasks(String userId, AddTaskDto addTaskDto);

    /**
     * Marks a task as completed for a specific user.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the completed task
     */
    Mono<IdResponseDto> completeTask(String userId, String taskId);

    /**
     * Unassigns a specific task from a user.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task
     * @return a {@link Mono} emitting an {@link UpdateResponseDto} with matched and modified counts
     */
    Mono<UpdateResponseDto> deleteAssignedTask(String userId, String taskId);

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the deleted user,
     *         or an error if the user does not exist
     */
    Mono<IdResponseDto> deleteUser(String id);
}
