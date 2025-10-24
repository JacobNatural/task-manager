package com.app.taskmanager.repository.model;

/**
 * Enumeration representing the possible statuses of a task.
 */
public enum Status {
    /**
     * Task is created but not yet started.
     */
    TO_DO,

    /**
     * Task is currently in progress.
     */
    IN_PROGRESS,

    /**
     * Task has been completed.
     */
    DONE
}
