package com.app.taskmanager.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.NotBlank;
import java.util.List;

/**
 * DTO representing a request to add tasks to a user.
 * <p>
 * Contains a list of task IDs that should be assigned.
 */
public record AddTaskDto(

        /**
         * List of task IDs to add.
         * <p>
         * Must contain at least one task ID. Each ID must be non-blank.
         */
        @Schema(example = "[\"68f0d968af9623a741efba36\"]")
        @NotEmpty(message = "Add at least one task ID to the list.")
        List<@NotBlank(message = "Provide a valid task ID.") String> taskIds) {
}
