package com.app.taskmanager.dto.response;

import java.time.Instant;

/**
 * Generic response DTO used to wrap all API responses.
 * <p>
 * Contains the response data, a timestamp indicating when the response was created,
 * and a message (success or error).
 *
 * @param <T> the type of the response data
 */
public record ResponseDto<T>(T data, Instant timestamp, String message) {

    /**
     * Creates a successful response with the given data.
     * <p>
     * Sets the timestamp to the current instant and the message to "OK".
     *
     * @param data the response data
     */
    public ResponseDto(T data) {
        this(data, Instant.now(), "success");
    }

    /**
     * Creates an error response with the given message.
     * <p>
     * Sets the timestamp to the current instant and the data to {@code null}.
     *
     * @param errorMessage the error message
     */
    public ResponseDto(String errorMessage) {
        this(null, Instant.now(), errorMessage);
    }
}
