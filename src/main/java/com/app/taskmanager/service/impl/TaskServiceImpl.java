package com.app.taskmanager.service.impl;

import com.app.taskmanager.EntityNotFoundException;
import com.app.taskmanager.dto.create.CreateTaskDto;
import com.app.taskmanager.dto.create.UpdateTaskDto;
import com.app.taskmanager.dto.filters.FilterDto;
import com.app.taskmanager.dto.response.IdResponseDto;
import com.app.taskmanager.dto.response.PageResponseDto;
import com.app.taskmanager.dto.response.TaskResponseDto;
import com.app.taskmanager.dto.response.UpdateResponseDto;
import com.app.taskmanager.repository.TaskRepository;
import com.app.taskmanager.repository.model.Status;
import com.app.taskmanager.repository.model.Task;
import com.app.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

/**
 * Service implementation for managing tasks.
 * <p>
 * Provides methods for creating, updating, retrieving, assigning, completing,
 * unassigning, and deleting tasks.
 */
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task
     * @return a {@link Mono} emitting the {@link TaskResponseDto} if found, or an error if not found
     */
    public Mono<TaskResponseDto> findTaskByID(String id) {
        return taskRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Task not found.")))
                .map(Task::toResponseTaskDto);
    }

    /**
     * Retrieves all tasks with optional filtering and pagination.
     *
     * @param page   the page number (zero-based)
     * @param size   the number of tasks per page
     * @param filter the {@link FilterDto} containing filtering criteria
     * @return a {@link Mono} emitting a {@link PageResponseDto} of {@link TaskResponseDto}
     */
    @Override
    public Mono<PageResponseDto<TaskResponseDto>> findAllTasks(
            long page, long size, FilterDto filter) {


        return taskRepository.findWithPaginationAndFilter(size, page, filter.filterCriteria())
                .map(t ->
                        new PageResponseDto<>(
                                t.elements().stream().map(Task::toResponseTaskDto).toList(),
                                t.countInfo().isEmpty() ? 0 : t.countInfo().get(0).totalCount(), page, size)
                );
    }

    /**
     * Creates a new task.
     *
     * @param task the {@link CreateTaskDto} containing task information
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the created task
     */
    public Mono<IdResponseDto> createTask(CreateTaskDto task) {
        return taskRepository
                .save(task.toTaskModel())
                .map(t -> new IdResponseDto(t.getId()));
    }

    /**
     * Updates an existing task.
     *
     * @param id   the ID of the task to update
     * @param task the {@link UpdateTaskDto} containing updated information
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the updated task
     */
    public Mono<IdResponseDto> updateTask(String id, UpdateTaskDto task) {
        return taskRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Task not found.")))
                .flatMap(t -> {
                    t.setTitle(task.title());
                    t.setDescription(task.description());
                    t.setStatus(task.status());
                    return taskRepository.save(t);
                }).map(t -> new IdResponseDto(t.getId()));
    }

    /**
     * Unassigns all tasks from a user.
     *
     * @param userId the ID of the user
     * @return a {@link Mono} emitting an {@link UpdateResponseDto} with matched and modified counts
     */
    @Override
    public Mono<UpdateResponseDto> unassignUserTasks(String userId) {
        return taskRepository
                .unassignUserTasks(userId)
                .map(t -> new UpdateResponseDto(t.getMatchedCount(), t.getModifiedCount()));
    }

    /**
     * Unassigns a specific task from a user.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task
     * @return a {@link Mono} emitting an {@link UpdateResponseDto} with matched and modified counts
     */
    @Override
    public Mono<UpdateResponseDto> unassignUserTask(String userId, String taskId) {
        return taskRepository.unassignUserTask(userId, taskId)
                .flatMap(u -> {
                    if (u.getMatchedCount() == 0) {
                        return Mono.error(new EntityNotFoundException("Task not found."));
                    }
                    return Mono.just(new UpdateResponseDto(u.getMatchedCount(), u.getModifiedCount()));
                });
    }

    /**
     * Assigns a list of tasks to a user.
     *
     * @param userId the ID of the user
     * @param taskId the list of task IDs to assign
     * @return a {@link Mono} emitting a list of {@link IdResponseDto} for assigned tasks
     */
    @Override
    public Mono<List<IdResponseDto>> assignUserTasks(String userId, List<String> taskId) {
        return taskRepository
                .findAllById(taskId)
                .collectList()
                .publishOn(Schedulers.boundedElastic())
                .flatMap(tasks -> {

                    var allToDo = tasks.stream().allMatch(t -> t.getStatus() == Status.TO_DO);

                    if (tasks.size() < taskId.size()) {
                        return Mono.error(new EntityNotFoundException("Not all task were found."));
                    }

                    if (!allToDo) {
                        return Mono.error(new IllegalArgumentException(
                                "Cannot assign tasks: some tasks are not TODO."
                        ));
                    }

                    tasks.forEach(t -> {
                        t.setUserId(userId);
                        t.setStatus(Status.IN_PROGRESS);
                    });

                    return taskRepository.saveAll(tasks).map(t -> new IdResponseDto(t.getId())).collectList();
                });
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
        return taskRepository.findById(taskId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Task not found.")))
                .flatMap(t -> {

                    if (!t.getUserId().equals(userId)) {
                        return Mono.error(new IllegalArgumentException("Task is not assigned to user."));
                    }

                    if (t.getStatus() == Status.DONE) {
                        return Mono.error(new IllegalArgumentException("Task already completed."));
                    }

                    t.setStatus(Status.DONE);

                    return taskRepository.save(t)
                            .map(ts -> new IdResponseDto(ts.getId()));
                });
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     * @return a {@link Mono} emitting the {@link IdResponseDto} of the deleted task,
     * or an error if the task does not exist
     */
    @Override
    public Mono<IdResponseDto> deleteTask(String id) {
        return taskRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Task not found.")))
                .flatMap(t ->
                        taskRepository.delete(t)
                                .thenReturn(new IdResponseDto(id)));
    }
}
