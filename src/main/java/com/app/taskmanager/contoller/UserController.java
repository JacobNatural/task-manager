package com.app.taskmanager.contoller;

import com.app.taskmanager.configuration.SwaggerExampleObjects;
import com.app.taskmanager.dto.create.AddTaskDto;
import com.app.taskmanager.dto.create.CreateUserDto;
import com.app.taskmanager.dto.response.*;
import com.app.taskmanager.service.UserService;
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

import java.util.List;

/**
 * REST controller for managing users.
 * <p>
 * Provides reactive endpoints for creating, retrieving, updating, assigning tasks to,
 * and deleting users. All endpoints return responses wrapped in {@link reactor.core.publisher.Mono}.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves detailed information about a user by their unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieve a task by ID",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.USER_RESPONSE_DTO
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.USER_NOT_FOUND
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDto> findUserByID(
            @Parameter(description = "Unique identifier of the user", example = "68f4101f04083690f4a2df13")
            @PathVariable String id) {
        return userService.findUserByID(id);
    }

    @Operation(
            summary = "Get user by username",
            description = "Fetches user information using the username."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieve a task by username",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.USER_RESPONSE_DTO
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.USER_NOT_FOUND
                            )
                    )
            )
    })
    @GetMapping("/by-username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponseDto> findUserByUsername(
            @Parameter(description = "Username of the user", example = "SkyForest")
            @PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    @Operation(
            summary = "Get all users with pagination",
            description = "Returns a paginated list of users including total count, page, and size."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.USER_RESPONSE_LIST
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
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<PageResponseDto<UserResponseDto>> findAllUsers(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of users per page", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        return userService.findAllUsers(page, size);
    }

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user and returns the ID of the created user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    SwaggerExampleObjects.ID_RESPONSE_DTO)
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
    public Mono<IdResponseDto> createUser(
            @Parameter(description = "User creation data")
            @Valid @RequestBody CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }

    @Operation(
            summary = "Assign tasks to user",
            description = "Assigns one or more existing tasks to a specific user and returns a list of assigned task IDs."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tasks assigned to user successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.ID_RESPONSE_DTO
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.USER_NOT_FOUND
                            )
                    )
            )
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseDto<List<IdResponseDto>>> addTasks(
            @Parameter(description = "User ID to assign tasks to", example = "68f4101f04083690f4a2df13")
            @PathVariable String id,
            @Parameter(description = "Object containing list of task IDs to assign")
            @Valid @RequestBody AddTaskDto addTaskDto) {
        return userService
                .addTasks(id, addTaskDto)
                .map(ResponseDto::new);
    }

    @Operation(
            summary = "Remove assigned task from user",
            description = "Removes a specific task previously assigned to a user and returns confirmation."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task removed from user successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.UPDATE_RESPONSE
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.USER_NOT_FOUND
                            )
                    )
            )
    })
    @PatchMapping("/{userId}/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UpdateResponseDto> deleteAssignedTask(
            @Parameter(description = "User ID", example = "68f4101f04083690f4a2df13")
            @PathVariable String userId,
            @Parameter(description = "Task ID to remove", example = "68f245b1d494b40b89286165")
            @PathVariable String taskId) {
        return userService.deleteAssignedTask(userId, taskId);
    }

    @Operation(
            summary = "Mark task as completed",
            description = "Marks a specific task as completed for the given user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.ID_RESPONSE_DTO
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.USER_NOT_FOUND
                            )
                    )
            )
    })
    @PatchMapping("/complete/{userId}/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<IdResponseDto> completeTask(
            @Parameter(description = "User ID", example = "68f4101f04083690f4a2df13")
            @PathVariable String userId,
            @Parameter(description = "Task ID to mark as completed", example = "68f245b1d494b40b89286165")
            @PathVariable String taskId) {
        return userService.completeTask(userId, taskId);
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Permanently removes a user from the system by their unique ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.ID_RESPONSE_DTO
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = SwaggerExampleObjects.USER_NOT_FOUND
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<IdResponseDto> deleteUser(
            @Parameter(description = "User ID to delete", example = "68f4101f04083690f4a2df13")
            @PathVariable String id) {
        return userService.deleteUser(id);
    }
}
