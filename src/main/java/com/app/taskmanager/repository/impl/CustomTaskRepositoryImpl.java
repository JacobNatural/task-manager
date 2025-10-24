package com.app.taskmanager.repository.impl;

import com.app.taskmanager.repository.CustomTaskRepository;
import com.app.taskmanager.repository.view.TaskWithPaginationAndFilterView;
import com.app.taskmanager.repository.model.Status;
import com.app.taskmanager.repository.model.Task;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

/**
 * Implementation of {@link CustomTaskRepository} using {@link ReactiveMongoTemplate}.
 * <p>
 * Provides methods for retrieving tasks with pagination and filtering,
 * as well as unassigning tasks from users.
 */
@Repository
@RequiredArgsConstructor
public class CustomTaskRepositoryImpl implements CustomTaskRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    /**
     * Retrieves tasks with pagination and optional filtering by user, status, and date range.
     *
     * @param size     the number of tasks per page
     * @param page     the page number (zero-based)
     * @param userId   optional ID of the user to filter tasks by
     * @param status   optional status to filter tasks by
     * @param fromTime optional lower bound for task creation date
     * @param toTime   optional upper bound for task creation date
     * @return a {@link Mono} emitting a {@link TaskWithPaginationAndFilterView} containing tasks and count information
     */
    @Override
    public Mono<TaskWithPaginationAndFilterView> findTaskWithPaginationAndFilter(
            long size, long page, String userId, Status status, LocalDateTime fromTime, LocalDateTime toTime) {
        var agg = Aggregation.newAggregation(
                Aggregation.facet(
                                Aggregation.match(buildCriteria(userId, status, fromTime, toTime)),
                                Aggregation.skip(size * page),
                                Aggregation.limit(size)
                        ).as("tasks")
                        .and(
                                Aggregation.match(buildCriteria(userId, status, fromTime, toTime)),
                                Aggregation.count().as("totalCount")
                        ).as("countInfo")
        );
        return mongoTemplate.aggregate(agg, "tasks", TaskWithPaginationAndFilterView.class).next();
    }

    /**
     * Unassigns all tasks currently assigned to a specific user.
     *
     * @param userId the ID of the user whose tasks will be unassigned
     * @return a {@link Mono} emitting an {@link com.mongodb.client.result.UpdateResult} with the update result
     */
    @Override
    public Mono<UpdateResult> unassignUserTasks(String userId) {
        var query = new Query(Criteria.where("userId").is(userId));
        var update = new Update().set("userId", null);
        return mongoTemplate.updateMulti(query, update, Task.class);
    }

    /**
     * Unassigns a specific task from a specific user.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task to unassign
     * @return a {@link Mono} emitting an {@link com.mongodb.client.result.UpdateResult} with the update result
     */
    @Override
    public Mono<UpdateResult> unassignUserTask(String userId, String taskId) {
        var query = new Query(Criteria.where("userId").is(userId).and("id").is(taskId));
        var update = new Update().set("userId", null);
        return mongoTemplate.updateFirst(query, update, Task.class);
    }

    /**
     * Builds a dynamic {@link Criteria} object for filtering tasks by user, status, and creation date range.
     *
     * @param userId   optional ID of the user
     * @param status   optional status of the task
     * @param fromTime optional lower bound for creation date
     * @param toTime   optional upper bound for creation date
     * @return the constructed {@link Criteria} for query filtering
     */
    private Criteria buildCriteria(String userId, Status status, LocalDateTime fromTime, LocalDateTime toTime) {
        var criteria = new Criteria();

        if (userId != null && !userId.isEmpty()) {
            criteria.and("userId").is(userId);
        }

        if (status != null) {
            criteria.and("status").is(status);
        }

        if (fromTime != null) {
            criteria.and("creationDate").gte(fromTime);
        }

        if (toTime != null) {
            criteria.and("creationDate").gte(toTime);
        }
        return criteria;
    }
}
