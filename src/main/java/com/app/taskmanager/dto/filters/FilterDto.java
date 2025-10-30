package com.app.taskmanager.dto.filters;

import java.util.List;

/**
 * Data Transfer Object representing a set of filter criteria.
 * <p>
 * Encapsulates a list of {@link FilterCriteriaDto} objects to be applied
 * together when performing filtered queries.
 *
 * @param filterCriteria the list of filter criteria to apply
 */
public record FilterDto(List<FilterCriteriaDto<?>> filterCriteria) { }
