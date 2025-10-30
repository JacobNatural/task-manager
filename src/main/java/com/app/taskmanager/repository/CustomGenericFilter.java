package com.app.taskmanager.repository;

import com.app.taskmanager.dto.filters.FilterCriteriaDto;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Generic repository interface for performing filtered and paginated queries.
 * <p>
 * Provides a contract for fetching data with dynamic filtering and pagination.
 *
 * @param <T> the type of the response object returned by the query
 */
public interface CustomGenericFilter<T> {

    /**
     * Retrieves entities with pagination and filtering based on the provided criteria.
     *
     * @param size    the number of elements per page
     * @param page    the page number (zero-based)
     * @param filter  a list of {@link FilterCriteriaDto} representing filtering conditions
     * @return a {@link Mono} emitting the paginated and filtered result of type {@code T}
     */
    Mono<T> findWithPaginationAndFilter(long size, long page, List<FilterCriteriaDto<?>> filter);
}
