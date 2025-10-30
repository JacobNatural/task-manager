package com.app.taskmanager.repository.generic;

import com.app.taskmanager.dto.filters.FilterCriteriaDto;
import com.app.taskmanager.repository.model.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract generic repository providing pagination and filtering capabilities.
 * <p>
 * Implements the {@link com.app.taskmanager.repository.CustomGenericFilter} interface
 * and provides a base implementation for executing paginated and filtered queries
 * using {@link ReactiveMongoTemplate}.
 *
 * @param <T> the type of the result object returned by the query
 */
@RequiredArgsConstructor
public abstract class CustomGenericFilter<T> implements com.app.taskmanager.repository.CustomGenericFilter<T> {

    private final ReactiveMongoTemplate mongoTemplate;

    /**
     * The class type of the result object {@code T}.
     */
    private final Class<T> tClass =
            (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    /**
     * The name of the MongoDB collection on which queries are executed.
     */
    private final String collectionName;

    /**
     * Retrieves entities from the collection with pagination and filtering.
     *
     * @param size   the number of elements per page
     * @param page   the page number (zero-based)
     * @param filter a list of {@link FilterCriteriaDto} representing filtering conditions
     * @return a {@link Mono} emitting the paginated and filtered result of type {@code T}
     */
    public Mono<T> findWithPaginationAndFilter(long size, long page, List<FilterCriteriaDto<?>> filter) {
        var agg = Aggregation.newAggregation(
                Aggregation.facet(
                                Aggregation.match(buildCriteria(filter)),
                                Aggregation.skip(size * page),
                                Aggregation.limit(size)
                        ).as("elements")
                        .and(
                                Aggregation.match(buildCriteria(filter)),
                                Aggregation.count().as("totalCount")
                        ).as("countInfo")
        );
        return mongoTemplate.aggregate(agg, collectionName, tClass).next();
    }

    /**
     * Builds a {@link Criteria} object based on the provided filtering conditions.
     * <p>
     * Each filter is converted to a {@link Criteria} and all are combined using {@link Criteria#andOperator(Criteria...)}.
     *
     * @param filters the list of {@link FilterCriteriaDto} to apply
     * @return the {@link Criteria} representing all combined filtering conditions
     */
    private Criteria buildCriteria(List<FilterCriteriaDto<?>> filters) {
        var criteriaList = new ArrayList<Criteria>();

        for (var filter : filters) {
            switch (filter.operation()) {
                case Operation.IS:
                    criteriaList.add(Criteria.where(filter.key()).is(filter.value()));
                    break;
                case Operation.GTE:
                    criteriaList.add(Criteria.where(filter.key()).gte(filter.value()));
                    break;
                case Operation.LTE:
                    criteriaList.add(Criteria.where(filter.key()).lte(filter.value()));
                    break;
                case Operation.GT:
                    criteriaList.add(Criteria.where(filter.key()).gt(filter.value()));
                    break;
                case Operation.LT:
                    criteriaList.add(Criteria.where(filter.key()).lt(filter.value()));
                    break;
                case Operation.REGEX:
                    criteriaList.add(Criteria.where(filter.key()).regex(filter.value().toString()));
                    break;
            }
        }
        return new Criteria().andOperator(criteriaList.toArray(Criteria[]::new));
    }
}
