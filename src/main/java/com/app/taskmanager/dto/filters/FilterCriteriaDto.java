package com.app.taskmanager.dto.filters;

import com.app.taskmanager.repository.model.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object representing a single filter criterion.
 * <p>
 * Used to define dynamic filtering conditions for queries,
 * specifying the field key, the value to compare, and the operation to perform.
 *
 * @param <T> the type of the value to filter by
 * @param key the name of the field to filter
 * @param value the value to compare the field against
 * @param operation the {@link Operation} to apply for filtering
 */
public record FilterCriteriaDto<T>(String key, T value, Operation operation) {}
