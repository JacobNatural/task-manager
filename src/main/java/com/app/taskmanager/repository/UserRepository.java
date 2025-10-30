package com.app.taskmanager.repository;

import com.app.taskmanager.repository.model.User;
import com.app.taskmanager.repository.view.UserWithPaginationAndFilterView;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * Repository interface for {@link User} entities.
 * <p>
 * Extends {@link ReactiveMongoRepository} to provide standard CRUD operations
 * and custom aggregation queries for pagination and counting.
 */
public interface UserRepository extends ReactiveMongoRepository<User, String>, CustomGenericFilter<UserWithPaginationAndFilterView> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return a {@link Mono} emitting the user if found, or empty if not
     */
    Mono<User> findByUsername(String username);

    /**
     * Retrieves users with pagination and total count using an aggregation pipeline.
     * <p>
     * The aggregation uses `$facet` to return both the paginated users and the total count of users.
     *
     * @param skip  the number of documents to skip
     * @param limit the maximum number of documents to return
     * @return a {@link Mono} emitting a {@link UserWithPaginationAndFilterView} containing the users and count information
     */
    @Aggregation(
            """
        { 
          $facet: { 
            users: [ { $skip: ?0 }, { $limit: ?1 } ], 
            countInfo: [ { $count: 'totalCount' } ] 
          } 
        }
    """
    )
    Mono<UserWithPaginationAndFilterView> findAllWithPaginationAndCount(int skip, int limit);
}
