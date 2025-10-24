package com.app.taskmanager.contoller;

import com.app.taskmanager.configuration.SwaggerExampleObjects;
import com.app.taskmanager.dto.create.CreateTaskDto;
import com.app.taskmanager.dto.create.UpdateTaskDto;
import com.app.taskmanager.dto.response.PageResponseDto;
import com.app.taskmanager.dto.response.IdResponseDto;
import com.app.taskmanager.dto.response.TaskResponseDto;
import com.app.taskmanager.repository.model.Status;
import com.app.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * REST controller for managing tasks.
 * <p>
 * Provides reactive endpoints for creating, updating, retrieving, listing, and deleting tasks.
 * All responses are returned as {@link reactor.core.publisher.Mono} instances.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Retrieve a task by ID",
            description = "Returns detailed information about a specific task using its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tasks found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.TASK_RESPONSE_DTO
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.TASK_NOT_FOUND
                            )
                    )
            )
    })
    public Mono<TaskResponseDto> findTask(
            @Parameter(description = "Unique identifier of the task", example = "68f4101f04083690f4a2df13")
            @PathVariable String id) {
        return taskService.findTaskByID(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Retrieve all tasks with optional filters",
            description = "Fetches a paginated list of tasks. You can filter results by task status, assigned user, or creation date range."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tasks retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.TASK_RESPONSE_LIST
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.INTERNAL_ERROR
                            )
                    )
            )
    })
    public Mono<PageResponseDto<TaskResponseDto>> findAllTasks(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of tasks per page", example = "2")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Optional filter by task status", example = "TO_DO")
            @RequestParam(required = false) Status status,
            @Parameter(description = "Optional filter by assigned user ID", example = "68f4101f04083690f4a2df13")
            @RequestParam(required = false) String userId,
            @Parameter(description = "Optional filter: include tasks created from this date", example = "2025-9-22T00:00:00")
            @RequestParam(required = false) LocalDateTime dataFrom,
            @Parameter(description = "Optional filter: include tasks created up to this date", example = "2025-11-23T23:59:59")
            @RequestParam(required = false) LocalDateTime dataTo) {
        return taskService.findAllTasks(page, size, userId, status, dataFrom, dataTo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new task",
            description = "Creates a new task and returns its unique ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.ID_RESPONSE_DTO
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input format",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.BAD_REQUEST
                            )
                    )
            )
    })
    public Mono<IdResponseDto> createTask(
            @Parameter(description = "Task creation payload")
            @Valid @RequestBody CreateTaskDto task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update an existing task",
            description = "Updates an existing task by its ID and returns the updated task ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.ID_RESPONSE_DTO
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.INTERNAL_ERROR
                            )
                    )
            )
    })
    public Mono<IdResponseDto> updateTask(
            @Parameter(description = "ID of the task to update", example = "68f0d968af9623a741efba36")
            @PathVariable String id,
            @Valid @RequestBody UpdateTaskDto task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.ID_RESPONSE_DTO
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.TASK_NOT_FOUND
                            )
                    )
            )
    })
    public Mono<IdResponseDto> deleteTask(
            @Parameter(description = "ID of the task to delete", example = "68f0d968af9623a741efba36")
            @PathVariable String id) {
        return taskService.deleteTask(id);
    }
}
