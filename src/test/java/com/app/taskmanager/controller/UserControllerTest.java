package com.app.taskmanager.controller;

import com.app.taskmanager.EntityNotFoundException;
import com.app.taskmanager.contoller.UserController;
import com.app.taskmanager.dto.create.AddTaskDto;
import com.app.taskmanager.dto.create.CreateUserDto;
import com.app.taskmanager.dto.response.*;
import com.app.taskmanager.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Should return 201 CREATED and user ID when creating a new user.")
    public void test1() {

        var createUser = new CreateUserDto("firstName", "lastName", "username");

        Mockito.when(userService.createUser(Mockito.any(CreateUserDto.class)))
                .thenReturn(Mono.just(new IdResponseDto("1ABB22")));

        webClient
                .post()
                .uri("/users")
                .bodyValue(createUser)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(IdResponseDto.class)
                .value(r -> assertEquals("1ABB22", r.id()));

        Mockito.verify(userService, Mockito.times(1))
                .createUser(createUser);
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when trying to create a user with invalid input data.")
    public void test2() {

        var createUser = new CreateUserDto("", "A..1", null);

        webClient
                .post()
                .uri("/users")
                .bodyValue(createUser)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Should return 200 OK and the user details when getting user by ID.")
    public void test3() {

        var userResponse = new UserResponseDto("1ABB22", "firstName", "lastName", "username");

        Mockito.when(userService.findUserByID(Mockito.anyString()))
                .thenReturn(Mono.just(userResponse));

        webClient
                .get()
                .uri("/users/1ABB22")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .value(r -> {
                    assertEquals(userResponse.id(), r.id());
                    assertEquals(userResponse.name(), r.name());
                    assertEquals(userResponse.username(), r.username());
                });

        Mockito.verify(userService, Mockito.times(1))
                .findUserByID("1ABB22");
    }

    @Test
    @DisplayName("Should return 200 OK and the user details when getting user by username.")
    public void test4() {

        var userResponse = new UserResponseDto("1ABB22", "firstName", "lastName", "username");

        Mockito.when(userService.findUserByUsername(Mockito.anyString()))
                .thenReturn(Mono.just(userResponse));

        webClient
                .get()
                .uri("/users/by-username/username")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .value(r -> {
                    assertEquals(userResponse.id(), r.id());
                    assertEquals(userResponse.name(), r.name());
                    assertEquals(userResponse.username(), r.username());
                });

        Mockito.verify(userService, Mockito.times(1))
                .findUserByUsername("username");
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when trying to get a user by non-existent username.")
    public void test5() {

        Mockito.when(userService.findUserByUsername(Mockito.anyString()))
                .thenReturn(Mono.error(new EntityNotFoundException("User not found.")));

        webClient
                .get()
                .uri("/users/by-username/username")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User not found.")
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();

    }

    @Test
    @DisplayName("Should return 200 OK and paginated list of users when valid pagination parameters are provided.")
    public void test6() {

        var list = new ArrayList<>(List.of(
                new UserResponseDto("1ABB22", "firstName", "lastName", "username")));

        var pageResponse = new PageResponseDto<>(list, 5, 1, 1);

        Mockito.when(userService.findAllUsers(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Mono.just(pageResponse));

        webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .queryParam("page", "1")
                        .queryParam("size", "1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponseDto<UserResponseDto>>() {
                })
                .value(r -> {
                    assertEquals(pageResponse.page(), r.page());
                    assertEquals(pageResponse.size(), r.size());
                    assertEquals(pageResponse.total(), r.total());
                    assertEquals(pageResponse.list().get(0), r.list().get(0));
                });

        Mockito.verify(userService, Mockito.times(1))
                .findAllUsers(1, 1);
    }

    @Test
    @DisplayName("Should return 200 OK and paginated list of users when default pagination parameters are used.")
    public void test7() {

        var list = new ArrayList<>(List.of(
                new UserResponseDto("1ABB22", "firstName", "lastName", "username")));

        var pageResponse = new PageResponseDto<>(list, 5, 0, 1);

        Mockito.when(userService.findAllUsers(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Mono.just(pageResponse));

        webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponseDto<UserResponseDto>>() {
                })
                .value(r -> {
                    assertEquals(pageResponse.page(), r.page());
                    assertEquals(pageResponse.size(), r.size());
                    assertEquals(pageResponse.total(), r.total());
                    assertEquals(pageResponse.list().get(0), r.list().get(0));
                });

        Mockito.verify(userService, Mockito.times(1))
                .findAllUsers(0, 20);
    }

    @Test
    @DisplayName("Should return 200 OK and list of added task IDs when assigning tasks to user.")
    public void test8() {

        var ids = List.of("1TAA21", "1TAB22", "1TAC23");
        var taskIds = List.of(
                new IdResponseDto("1TAA21"), new IdResponseDto("1TAB22"), new IdResponseDto("1TAC23"));
        var addTaskDto = new AddTaskDto(ids);

        Mockito.when(userService.addTasks(Mockito.anyString(), Mockito.any()))
                .thenReturn(Mono.just(taskIds));

        webClient
                .patch()
                .uri("/users/1ABB22")
                .bodyValue(addTaskDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResponseDto<List<IdResponseDto>>>() {
                })
                .value(r -> {
                    assertEquals(3, r.data().size());
                    assertEquals(taskIds.get(0), r.data().get(0));
                });

        Mockito.verify(userService, Mockito.times(1))
                .addTasks("1ABB22", addTaskDto);
    }

    @Test
    @DisplayName("should return 404 NOT FOUND when trying to assign tasks to non-existent user.")
    public void test9() {

        var ids = List.of("1TAA21", "1TAB22", "1TAC23");
        var addTaskDto = new AddTaskDto(ids);

        Mockito.when(userService.addTasks(Mockito.anyString(), Mockito.any()))
                .thenReturn(Mono.error(new EntityNotFoundException("User not found.")));

        webClient
                .patch()
                .uri("/users/1ABB22")
                .bodyValue(addTaskDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User not found.")
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();

    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when assigning tasks with empty task list.")
    public void test10() {

        List<String> ids = List.of();
        var addTaskDto = new AddTaskDto(ids);

        webClient
                .patch()
                .uri("/users/1ABB22")
                .bodyValue(addTaskDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();

    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when assigning tasks with invalid task ID in list.")
    public void test11() {

        List<String> ids = List.of("");
        var addTaskDto = new AddTaskDto(ids);

        webClient
                .patch()
                .uri("/users/1ABB22")
                .bodyValue(addTaskDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Should return 200 OK and update statistics when unassigning a task from user.")
    public void test12() {

        var updateResponseDto = new UpdateResponseDto(1, 1);

        Mockito.when(userService.deleteAssignedTask(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(updateResponseDto));

        webClient
                .patch()
                .uri("/users/userId/taskId")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UpdateResponseDto.class)
                .value(r -> {
                    assertEquals(updateResponseDto.matched(), r.matched());
                    assertEquals(updateResponseDto.modified(), r.modified());
                });

        Mockito.verify(userService, Mockito.times(1))
                .deleteAssignedTask("userId", "taskId");
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when unassigning a task from non-existent user.")
    public void test13() {

        var updateResponseDto = new UpdateResponseDto(1, 1);

        Mockito.when(userService.deleteAssignedTask(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.error(new EntityNotFoundException("User not found.")));

        webClient
                .patch()
                .uri("/users/userId/taskId")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User not found.")
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when unassigning non-existent task from user.")
    public void test14() {

        Mockito.when(userService.deleteAssignedTask(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.error(new EntityNotFoundException("Task not found.")));

        webClient
                .patch()
                .uri("/users/userId/taskId")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Task not found.")
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Should return 200 OK and completed task ID when changing task status to done.")
    public void test15() {

        var idResponseDto = new IdResponseDto("taskId");

        Mockito.when(userService.completeTask(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(idResponseDto));

        webClient
                .patch()
                .uri("/users/complete/userId/taskId")
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponseDto.class)
                .value(r -> {
                    assertEquals(idResponseDto.id(), r.id());
                });

        Mockito.verify(userService, Mockito.times(1))
                .completeTask("userId", "taskId");
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when changing task status to done for non-existent user.")
    public void test16() {

        Mockito.when(userService.completeTask(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.error(new EntityNotFoundException("User not found.")));

        webClient
                .patch()
                .uri("/users/complete/userId/taskId")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User not found.")
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when changing status of non-existent task to done.")
    public void test17() {

        Mockito.when(userService.completeTask(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.error(new EntityNotFoundException("Task not found.")));

        webClient
                .patch()
                .uri("/users/complete/userId/taskId")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Task not found.")
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Should return 204 NO CONTENT when deleting existing user.")
    public void test18() {

        var idResponseDto = new IdResponseDto("userId");

        Mockito.when(userService.deleteUser(Mockito.anyString()))
                .thenReturn(Mono.just(idResponseDto));

        webClient
                .delete()
                .uri("/users/userId")
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponseDto.class)
                .value(r -> assertEquals(idResponseDto.id(), r.id()));

        Mockito.verify(userService, Mockito.times(1))
                .deleteUser("userId");
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when deleting non-existent user.")
    public void test19() {

        Mockito.when(userService.deleteUser(Mockito.anyString()))
                .thenReturn(Mono.error(new EntityNotFoundException("User not found.")));

        webClient
                .delete()
                .uri("/users/userId")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User not found.")
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();
    }
}
