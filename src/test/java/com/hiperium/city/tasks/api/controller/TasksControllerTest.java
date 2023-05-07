package com.hiperium.city.tasks.api.controller;

import com.hiperium.city.tasks.api.common.AbstractContainerBase;
import com.hiperium.city.tasks.api.dto.TaskCriteriaDto;
import com.hiperium.city.tasks.api.dto.TaskDto;
import com.hiperium.city.tasks.api.model.Task;
import com.hiperium.city.tasks.api.utils.TaskUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TasksControllerTest extends AbstractContainerBase {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    @DisplayName("Find all Tasks")
    void givenTasksList_whenFindAllTasks_thenReturnTasksList() {
        this.webTestClient
                .get()
                .uri(TaskUtil.TASK_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(TaskDto.class)
                .value(taskList -> {
                    Assertions.assertThat(taskList).isNotEmpty();
                    Assertions.assertThat(taskList).hasSize(4);
                });
    }

    @Test
    @Order(2)
    @DisplayName("Find Task by ID")
    void givenTaskId_whenFindTaskById_thenReturnTaskFound() {
        TaskCriteriaDto criteriaDto = TaskCriteriaDto.builder()
                .id(1L)
                .build();
        this.webTestClient
                .post()
                .uri(TaskUtil.TASKS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(criteriaDto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Task.class)
                .value(taskList -> {
                    Assertions.assertThat(taskList).isNotEmpty();
                    Assertions.assertThat(taskList).hasSize(4);
                });
    }

    @Test
    @DisplayName("Find Task that does not exist")
    void givenNotExistingTasksId_whenFindTaskById_thenReturnError404() {
        TaskCriteriaDto taskCriteriaDto = TaskCriteriaDto.builder()
                .id(101L)
                .build();
        this.webTestClient
                .post()
                .uri(TaskUtil.TASKS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(taskCriteriaDto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(TaskDto.class)
                .value(taskList -> Assertions.assertThat(taskList).isEmpty());
    }
}
