package com.app.taskmanager;

/**
 * Exception thrown when a requested entity is not found in the system.
 * <p>
 * This exception can be used across repositories and services to signal that
 * a specific entity does not exist.
 */
public class EntityNotFoundException extends Exception {

    /**
     * Constructs a new {@code EntityNotFoundException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
