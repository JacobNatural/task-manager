package com.app.taskmanager.repository.impl;

import com.app.taskmanager.repository.CustomTaskRepository;
import com.app.taskmanager.repository.model.Task;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

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
     * Unassigns all tasks currently assigned to a specific user.
     *
     * @param userId the ID of the user whose tasks will be unassigned
     * @return a {@link Mono} emitting an {@link UpdateResult} with the update result
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
     * @return a {@link Mono} emitting an {@link UpdateResult} with the update result
     */
    @Override
    public Mono<UpdateResult> unassignUserTask(String userId, String taskId) {
        var query = new Query(Criteria.where("userId").is(userId).and("id").is(taskId));
        var update = new Update().set("userId", null);
        return mongoTemplate.updateFirst(query, update, Task.class);
    }
}
