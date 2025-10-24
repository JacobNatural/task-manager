package com.app.taskmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO containing a single entity ID.
 * <p>
 * Typically used to return the ID of a newly created or updated resource.
 */
public record IdResponseDto(String id) {
}
