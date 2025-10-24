package com.app.taskmanager.controller;

import com.app.taskmanager.EntityNotFoundException;
import com.app.taskmanager.contoller.TaskController;
import com.app.taskmanager.dto.create.CreateTaskDto;
import com.app.taskmanager.dto.create.UpdateTaskDto;
import com.app.taskmanager.dto.response.IdResponseDto;
import com.app.taskmanager.dto.response.PageResponseDto;
import com.app.taskmanager.dto.response.TaskResponseDto;
import com.app.taskmanager.repository.model.Status;
import com.app.taskmanager.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebFluxTest(controllers = TaskController.class)
public class TaskControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private TaskService taskService;

    @Test
    @DisplayName("Should return 200 OK and the task when requesting a task by ID.")
    public void test1() {

        var task = new TaskResponseDto("taskID", "Title", "Description", LocalDateTime.now(),
                Status.TO_DO, null);

        Mockito.when(taskService.findTaskByID(Mockito.anyString()))
                .thenReturn(Mono.just(task));

        webClient
                .get()
                .uri("/tasks/taskID")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponseDto.class)
                .value(r -> {
                    assertEquals(task.id(), r.id());
                    assertEquals(task.title(), r.title());
                    assertEquals(task.description(), r.description());
                    assertEquals(task.status(), r.status());
                    assertNotNull(r.creationDate());
                });

        Mockito.verify(taskService, Mockito.times(1))
                .findTaskByID("taskID");
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when requesting a task by a non-existent ID.")
    public void test2() {

        Mockito.when(taskService.findTaskByID(Mockito.anyString()))
                .thenReturn(Mono.error(new EntityNotFoundException("Task not found.")));

        webClient
                .get()
                .uri("/tasks/A23VBN")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Task not found.")
                .jsonPath("$.data").doesNotExist()
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Should return 200 OK and a paginated list of tasks when requesting tasks with filter and pagination parameters.")
    public void test3() {

        var task1 = new TaskResponseDto("testID1", "Title1", "Description1", LocalDateTime.now(),
                Status.TO_DO, null);
        var task2 = new TaskResponseDto("testID2", "Title2", "Description2", LocalDateTime.now(),
                Status.TO_DO, null);

        var pageTaskResponse = new PageResponseDto<>(List.of(task1, task2), 5, 0, 2);

        Mockito.when(taskService.findAllTasks(0, 2, null, null, null, null))
                .thenReturn(Mono.just(pageTaskResponse));

        webClient
                .get()
                .uri(uri -> uri
                        .path("/tasks")
                        .queryParam("page", "0")
                        .queryParam("size", 2)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponseDto<TaskResponseDto>>() {
                })
                .value(r -> {
                    assertEquals(2, r.list().size());
                    assertEquals(task1.id(), r.list().get(0).id());
                    assertEquals(task2.id(), r.list().get(1).id());
                    assertEquals(0, r.page());
                    assertEquals(2, r.size());
                    assertEquals(5, r.total());
                });


        Mockito.verify(taskService, Mockito.times(1))
                .findAllTasks(0, 2, null, null, null, null);
    }

    @Test
    @DisplayName("Should return 200 OK and a paginated list of tasks when requesting tasks with default pagination parameters.")
    public void test4() {

        var task1 = new TaskResponseDto("A23VBN", "Title", "Description", LocalDateTime.now(),
                Status.TO_DO, null);
        var task2 = new TaskResponseDto("A23VBN", "Title", "Description", LocalDateTime.now(),
                Status.TO_DO, null);

        var pageTaskResponse = new PageResponseDto<>(List.of(task1, task2), 5, 0, 2);

        Mockito.when(taskService.findAllTasks(0, 10, null, null, null, null))
                .thenReturn(Mono.just(pageTaskResponse));

        webClient
                .get()
                .uri(uri -> uri
                        .path("/tasks")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PageResponseDto<TaskResponseDto>>() {
                })
                .value(r -> {
                    assertEquals(2, r.list().size());
                    assertEquals(task1.id(), r.list().get(0).id());
                    assertEquals(task2.id(), r.list().get(1).id());
                    assertEquals(0, r.page());
                    assertEquals(2, r.size());
                    assertEquals(5, r.total());
                });


        Mockito.verify(taskService, Mockito.times(1))
                .findAllTasks(0, 10, null, null, null, null);
    }

    @Test
    @DisplayName("Should return 201 CREATED and the task ID when creating a new task.")
    public void test5() {

        var createTask = new CreateTaskDto("Title", "Description");
        var idResponse = new IdResponseDto("taskID");

        Mockito.when(taskService.createTask(createTask))
                .thenReturn(Mono.just(idResponse));

        webClient
                .post()
                .uri("/tasks")
                .bodyValue(createTask)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(IdResponseDto.class)
                .value(r -> assertEquals(idResponse.id(), r.id()));

        Mockito.verify(taskService, Mockito.times(1))
                .createTask(createTask);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when trying to create a task with invalid input data.")
    public void test6() {

        var createTask = new CreateTaskDto("Title<.>", "Description");

        webClient
                .post()
                .uri("/tasks")
                .bodyValue(createTask)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").exists()
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.data").doesNotExist();
    }

    @Test
    @DisplayName("Should return 200 OK and the task ID when updating a task.")
    public void test7() {

        var updateTaskDto = new UpdateTaskDto("Title", "Description", Status.TO_DO);
        var idResponse = new IdResponseDto("taskID");

        Mockito.when(taskService.updateTask("taskID", updateTaskDto))
                .thenReturn(Mono.just(idResponse));

        webClient
                .put()
                .uri("/tasks/taskID")
                .bodyValue(updateTaskDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponseDto.class)
                .value(r -> assertEquals(idResponse.id(), r.id()));

        Mockito.verify(taskService, Mockito.times(1))
                .updateTask("taskID", updateTaskDto);
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when trying to update a non-existent task.")
    public void test8() {

        var updateTaskDto = new UpdateTaskDto("Title", "Description", Status.TO_DO);
        var idResponse = new IdResponseDto("taskID");

        Mockito.when(taskService.updateTask("taskID", updateTaskDto))
                .thenReturn(Mono.error(new EntityNotFoundException("Task not found.")));

        webClient
                .put()
                .uri("/tasks/taskID")
                .bodyValue(updateTaskDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Task not found.")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.data").doesNotExist();

        Mockito.verify(taskService, Mockito.times(1))
                .updateTask("taskID", updateTaskDto);
    }

    @Test
    @DisplayName("Should return 400 Bad Request when updating a task with invalid input data.")
    public void test9() {

        var updateTaskDto = new UpdateTaskDto("Title<..>", "Description", Status.TO_DO);
        var idResponse = new IdResponseDto("taskID");

        webClient
                .put()
                .uri("/tasks/taskID")
                .bodyValue(updateTaskDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").exists()
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.data").doesNotExist();
    }

    @Test
    @DisplayName("Should return 204 OK when deleting a task.")
    public void test10() {

        var idResponse = new IdResponseDto("taskID");

        Mockito.when(taskService.deleteTask("taskID"))
                .thenReturn(Mono.just(idResponse));

        webClient
                .delete()
                .uri("/tasks/taskID")
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdResponseDto.class)
                .value(r -> assertEquals(idResponse.id(), r.id()));

        Mockito.verify(taskService, Mockito.times(1))
                .deleteTask("taskID");
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when trying to delete a non-existent task.")
    public void test11() {

        Mockito.when(taskService.deleteTask("taskID"))
                .thenReturn(Mono.error(new EntityNotFoundException("Task not found.")));

        webClient
                .delete()
                .uri("/tasks/taskID")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Task not found.")
                .jsonPath("$.timestamp").exists()
                .jsonPath("$.data").doesNotExist();
    }
}
