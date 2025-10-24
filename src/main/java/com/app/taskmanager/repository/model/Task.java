package com.app.taskmanager.repository.model;

import com.app.taskmanager.dto.response.TaskResponseDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents a task entity stored in the MongoDB collection "tasks".
 * <p>
 * Contains information about the task, such as title, description, creation date, status, and assigned user.
 */
@Data
@Document(collection = "tasks")
@Builder
public class Task {

    /**
     * Unique identifier of the task.
     */
    @Id
    private String id;

    /**
     * Title of the task.
     */
    private String title;

    /**
     * Description providing details about the task.
     */
    private String description;

    /**
     * Date and time when the task was created.
     */
    private LocalDateTime creationDate;

    /**
     * Current status of the task.
     */
    private Status status;

    /**
     * Identifier of the user to whom the task is assigned.
     */
    private String userId;

    /**
     * Converts this entity to a {@link TaskResponseDto}.
     *
     * @return a DTO representation of the task
     */
    public TaskResponseDto toResponseTaskDto() {
        return new TaskResponseDto(id, title, description, creationDate, status, userId);
    }
}
