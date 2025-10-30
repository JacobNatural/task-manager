package com.app.taskmanager.controller;

import com.app.taskmanager.swagger.SwaggerExampleObjects;
import com.app.taskmanager.dto.create.CreateTaskDto;
import com.app.taskmanager.dto.create.UpdateTaskDto;
import com.app.taskmanager.dto.filters.FilterDto;
import com.app.taskmanager.dto.response.IdResponseDto;
import com.app.taskmanager.dto.response.PageResponseDto;
import com.app.taskmanager.dto.response.TaskResponseDto;
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

/**
 * REST controller for managing tasks.
 * <p>
 * Provides reactive endpoints for creating, updating, retrieving, listing, and deleting tasks.
 * All responses are returned as {@link Mono} instances.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


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
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<TaskResponseDto> findTask(
            @Parameter(description = "Unique identifier of the task", example = "68f4101f04083690f4a2df13")
            @PathVariable String id) {
        return taskService.findTaskByID(id);
    }

    @Operation(
            summary = "Retrieve all tasks with optional filters",
            description = "Fetches a paginated list of tasks. You can filter results."
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
    @PostMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PageResponseDto<TaskResponseDto>> findAllTasks(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of tasks per page", example = "2")
            @RequestParam(defaultValue = "10") int size,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Filter criteria for users",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = SwaggerExampleObjects.TASK_FILTER_DTO)
                    )
            )
            @RequestBody FilterDto filter) {
        return taskService.findAllTasks(page, size, filter);
    }


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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<IdResponseDto> createTask(
            @Parameter(description = "Task creation payload")
            @Valid @RequestBody CreateTaskDto task) {
        return taskService.createTask(task);
    }


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
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<IdResponseDto> updateTask(
            @Parameter(description = "ID of the task to update", example = "68f0d968af9623a741efba36")
            @PathVariable String id,
            @Valid @RequestBody UpdateTaskDto task) {
        return taskService.updateTask(id, task);
    }


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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<IdResponseDto> deleteTask(
            @Parameter(description = "ID of the task to delete", example = "68f0d968af9623a741efba36")
            @PathVariable String id) {
        return taskService.deleteTask(id);
    }
}
