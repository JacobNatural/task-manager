package com.app.taskmanager.dto.create;

import com.app.taskmanager.repository.model.Status;
import com.app.taskmanager.repository.model.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * DTO representing a request to create a new task.
 * <p>
 * Contains the title and description of the task. Provides validation constraints
 * to ensure the fields are properly filled and formatted.
 */
public record CreateTaskDto(

        /**
         * Title of the task.
         * <p>
         * Must be non-blank and between 3 and 100 characters. Only letters, numbers,
         * spaces, and common punctuation are allowed.
         */
        @NotBlank(message = "Fill the title.")
        @Pattern(regexp = "^[\\p{L}0-9 ,.?!'\"()\\-]{3,100}$", message = "Wrong format of title.")
        @Schema(example = "Finish documentation")
        String title,

        /**
         * Description of the task.
         * <p>
         * Must be non-blank and between 3 and 500 characters. Only letters, numbers,
         * spaces, and common punctuation are allowed.
         */
        @NotBlank(message = "Fill the title")
        @Pattern(regexp = "^[\\p{L}0-9 ,.?!'\"()\\-]{3,500}$", message = "Wrong format of description.")
        @Schema(example = "Write Swagger examples for all endpoints")
        String description) {

    /**
     * Converts this DTO into a {@link com.app.taskmanager.repository.model.Task} entity.
     * <p>
     * Sets the creation date to the current date and the initial status to {@link com.app.taskmanager.repository.model.Status#TO_DO}.
     *
     * @return a new Task entity
     */
    public Task toTaskModel() {
        return Task.builder()
                .title(title)
                .description(description)
                .creationDate(LocalDateTime.now())
                .status(Status.TO_DO)
                .build();
    }
}
