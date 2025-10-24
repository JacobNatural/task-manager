package com.app.taskmanager.dto.create;

import com.app.taskmanager.repository.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO representing a request to update an existing task.
 * <p>
 * Contains the title, description, and status of the task.
 * Validation constraints ensure that fields are properly filled and formatted.
 */
public record UpdateTaskDto(

        /**
         * Title of the task.
         * <p>
         * Must be non-blank and between 3 and 100 characters. Only letters, numbers,
         * spaces, and common punctuation are allowed.
         */
        @NotBlank(message = "Fill the title.")
        @Pattern(regexp = "^[\\p{L}0-9 ,.?!'\"()\\-]{3,100}$", message = "Wrong format of title.")
        @Schema(example = "Change the documentation")
        String title,

        /**
         * Description of the task.
         * <p>
         * Must be non-blank and between 3 and 500 characters. Only letters, numbers,
         * spaces, and common punctuation are allowed.
         */
        @NotBlank(message = "Fill the title")
        @Pattern(regexp = "^[\\p{L}0-9 ,.?!'\"()\\-]{3,500}$", message = "Wrong format of description.")
        @Schema(example = "Check Swagger examples for all endpoints")
        String description,

        /**
         * Status of the task.
         * <p>
         * Must not be null. Uses the {@link com.app.taskmanager.repository.model.Status} enum.
         */
        @NotNull(message = "Fill the status.")
        @Schema(example = "IN_PROGRESS")
        Status status
) {
}
