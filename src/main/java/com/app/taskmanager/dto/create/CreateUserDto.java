package com.app.taskmanager.dto.create;

import com.app.taskmanager.repository.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO representing a request to create a new user.
 * <p>
 * Contains the name, surname, and username of the user. Includes validation constraints
 * to ensure fields are properly filled and formatted.
 */
public record CreateUserDto(

        /**
         * First name of the user.
         * <p>
         * Must be non-blank and between 2 and 30 characters. Only letters, spaces,
         * apostrophes, and hyphens are allowed.
         */
        @NotBlank(message = "Fill the name.")
        @Pattern(regexp = "^[\\p{L}\\s'-]{2,30}$", message = "Wrong format of name.")
        @Schema(example = "Jony")
        String name,

        /**
         * Surname of the user.
         * <p>
         * Must be non-blank and between 2 and 40 characters. Only letters, spaces,
         * apostrophes, and hyphens are allowed.
         */
        @NotBlank(message = "Fill the surname.")
        @Pattern(regexp = "^[\\p{L}\\s'-]{2,40}$", message = "Wrong format of surname.")
        @Schema(example = "Deep")
        String surname,

        /**
         * Username of the user.
         * <p>
         * Must be non-blank and between 2 and 30 characters. Only letters, numbers,
         * spaces, dots, underscores, and hyphens are allowed.
         */
        @NotBlank(message = "Fill the username.")
        @Pattern(regexp = "^[A-Za-z0-9\\s._-]{2,30}$", message = "Wrong format of username.")
        @Schema(example = "SkyForest")
        String username) {

    /**
     * Converts this DTO into a {@link com.app.taskmanager.repository.model.User} entity.
     *
     * @return a new User entity
     */
    public User toUser() {
        return User
                .builder()
                .name(name)
                .surname(surname)
                .username(username)
                .build();
    }
}
