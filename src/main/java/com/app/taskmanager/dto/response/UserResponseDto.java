package com.app.taskmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO representing a user.
 * <p>
 * Contains basic user information such as ID, name, surname, and username.
 */
public record UserResponseDto(
        /**
         * Unique identifier of the user.
         */

        String id,

        /**
         * User's first name.
         */

        String name,

        /**
         * User's surname.
         */

        String surname,

        /**
         * Unique username used for authentication or identification.
         */

        String username
) {
}
