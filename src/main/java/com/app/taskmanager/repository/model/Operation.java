package com.app.taskmanager.repository.model;

import com.app.taskmanager.dto.filters.FilterCriteriaDto;

/**
 * Enumeration of supported filter operations for dynamic queries.
 * <p>
 * Used in {@link FilterCriteriaDto} to specify
 * the type of comparison or matching to perform in queries.
 */
public enum Operation {
    /** Checks if a field is equal to a value. */
    IS,

    /** Checks if a field is greater than or equal to a value. */
    GTE,

    /** Checks if a field is less than or equal to a value. */
    LTE,

    /** Checks if a field is strictly greater than a value. */
    GT,

    /** Checks if a field is strictly less than a value. */
    LT,

    /** Checks if a field matches a regular expression. */
    REGEX
}
