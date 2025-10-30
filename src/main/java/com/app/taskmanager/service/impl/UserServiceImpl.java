package com.app.taskmanager.service.impl;

import com.app.taskmanager.EntityNotFoundException;
import com.app.taskmanager.dto.create.AddTaskDto;
import com.app.taskmanager.dto.create.CreateUserDto;
import com.app.taskmanager.dto.filters.FilterDto;
import com.app.taskmanager.dto.response.IdResponseDto;
import com.app.taskmanager.dto.response.PageResponseDto;
import com.app.taskmanager.dto.response.UpdateResponseDto;
import com.app.taskmanager.dto.response.UserResponseDto;
import com.app.taskmanager.repository.UserRepository;
import com.app.taskmanager.repository.model.User;
import com.app.taskmanager.service.TaskService;
import com.app.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service implementation for managing users.
 * <p>
 * Provides methods for creating, retrieving, updating, and deleting users,
 * as well as assigning, completing, and unassigning tasks for users.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TaskService taskService;

    /**
     * Creates a new user.
     *
     * @param user the {@link CreateUserDto} containing user information
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the created user
     */
    @Override
    public Mono<IdResponseDto> createUser(CreateUserDto user) {
        return userRepository
                .save(user.toUser())
                .map(u -> new IdResponseDto(u.getId()));
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return a {@link Mono} emitting the {@link UserResponseDto} if found, or an error if not found
     */
    @Override
    public Mono<UserResponseDto> findUserByID(String id) {
        return userRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found.")))
                .map(User::toUserResponseDto);
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user
     * @return a {@link Mono} emitting the {@link UserResponseDto} if found, or an error if not found
     */
    @Override
    public Mono<UserResponseDto> findUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found.")))
                .map(User::toUserResponseDto);
    }

    /**
     * Retrieves all users with pagination and optional filtering.
     *
     * @param page   the page number (zero-based)
     * @param size   the number of users per page
     * @param filter the {@link FilterDto} containing filtering criteria
     * @return a {@link Mono} emitting a {@link PageResponseDto} containing a list of {@link UserResponseDto},
     *         total number of users, and pagination details
     */
    @Override
    public Mono<PageResponseDto<UserResponseDto>> findAllUsers(int page, int size, FilterDto filter) {

        return userRepository.findWithPaginationAndFilter(size, page, filter.filterCriteria())
                .map(db -> new PageResponseDto(
                        db.elements().stream().map(User::toUserResponseDto).toList(),
                        db.countInfo().get(0).totalCount(),
                        page, size));
    }

    /**
     * Assigns tasks to a user.
     *
     * @param userId      the ID of the user
     * @param addTaskDto  the {@link AddTaskDto} containing task IDs to assign
     * @return a {@link Mono} emitting a list of {@link IdResponseDto} for assigned tasks
     */
    @Override
    public Mono<List<IdResponseDto>> addTasks(String userId, AddTaskDto addTaskDto) {
        return userRepository
                .findById(userId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found")))
                .flatMap(u -> taskService.assignUserTasks(userId, addTaskDto.taskIds()));
    }

    /**
     * Unassigns a specific task from a user.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task
     * @return a {@link Mono} emitting an {@link UpdateResponseDto} with matched and modified counts
     */
    @Override
    public Mono<UpdateResponseDto> deleteAssignedTask(String userId, String taskId) {
        return userRepository
                .findById(userId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found")))
                .flatMap(u -> taskService.unassignUserTask(userId, taskId));
    }

    /**
     * Marks a task as completed for a specific user.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the completed task
     */
    @Override
    public Mono<IdResponseDto> completeTask(String userId, String taskId) {
        return userRepository
                .findById(userId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found")))
                .flatMap(u -> taskService.completeTask(userId, taskId));
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the deleted user,
     *         or an error if the user does not exist
     */
    @Override
    public Mono<IdResponseDto> deleteUser(String id) {
        return userRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found")))
                .flatMap(u ->
                        taskService.unassignUserTasks(id)
                                .then(userRepository.deleteById(id))
                                .thenReturn(new IdResponseDto(id)));
    }
}
