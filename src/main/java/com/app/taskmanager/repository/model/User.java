package com.app.taskmanager.repository.model;

import com.app.taskmanager.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a user entity stored in the MongoDB collection "users".
 * <p>
 * Contains basic user information such as name, surname, and username.
 */
@Document(collection = "users")
@Data
@Builder
public class User {

    /**
     * Unique identifier of the user.
     */
    @Id
    String id;

    /**
     * First name of the user.
     */
    String name;

    /**
     * Surname of the user.
     */
    String surname;

    /**
     * Username used for authentication or identification.
     */
    String username;

    /**
     * Converts this entity to a {@link UserResponseDto}.
     *
     * @return a DTO representation of the user
     */
    public UserResponseDto toUserResponseDto() {
        return new UserResponseDto(id, name, surname, username);
    }
}
