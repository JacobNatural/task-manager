package com.app.taskmanager.dto.response;

import com.app.taskmanager.repository.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Response DTO representing a task.
 * <p>
 * Contains the task's ID, title, description, creation date, and current status.
 */
public record TaskResponseDto(
        /**
         * Unique identifier of the task.
         */
        String id,

        /**
         * Title of the task.
         */
        String title,

        /**
         * Description of the task.
         */
        String description,

        /**
         * Date and time when the task was created.
         */
        LocalDateTime creationDate,

        /**
         * Current status of the task.
         */
        Status status,

        /**
         * The user ID.
         */

        String userID
) {
}

