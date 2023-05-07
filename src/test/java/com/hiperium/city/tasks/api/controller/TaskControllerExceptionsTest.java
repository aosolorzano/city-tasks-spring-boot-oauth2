package com.hiperium.city.tasks.api.controller;

import com.hiperium.city.tasks.api.common.AbstractContainerBase;
import com.hiperium.city.tasks.api.dto.ErrorDetailsDto;
import com.hiperium.city.tasks.api.dto.TaskOperationDto;
import com.hiperium.city.tasks.api.utils.TaskUtil;
import com.hiperium.city.tasks.api.utils.enums.EnumTaskOperation;
import com.hiperium.city.tasks.api.utils.enums.EnumLanguageCode;
import com.hiperium.city.tasks.api.utils.enums.EnumResourceError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
@AutoConfigureWebTestClient
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerExceptionsTest extends AbstractContainerBase {

    @Autowired
    private WebTestClient webTestClient;

    private static TaskOperationDto taskOperationDto;

    @BeforeAll
    public static void init() {
        taskOperationDto = TaskUtil.getTaskOperationTemplate();
    }

    @Test
    @DisplayName("Update Tasks that does not exist")
    void givenNotExistingTasksId_whenUpdateTask_thenReturnError404() {
        taskOperationDto.setOperation(EnumTaskOperation.UPDATE);
        taskOperationDto.getTask().setId(201L);
        this.webTestClient
                .post()
                .uri(TaskUtil.TASK_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(taskOperationDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorDetailsDto.class)
                .value(errorDetailsDTO -> {
                    Assertions.assertThat(errorDetailsDTO.getErrorCode())
                            .isEqualTo(EnumResourceError.TASK_NOT_FOUND.getCode());
                    Assertions.assertThat(errorDetailsDTO.getErrorMessage())
                            .isEqualTo("Task not found with ID: " + taskOperationDto.getTask().getId() + ".");
                });
    }

    @Test
    @DisplayName("Update Tasks that does not exist - Spanish")
    void givenNotExistingTasksId_whenUpdateTask_thenReturnError404InSpanish() {
        taskOperationDto.setOperation(EnumTaskOperation.UPDATE);
        taskOperationDto.getTask().setId(202L);
        this.webTestClient
                .post()
                .uri(TaskUtil.TASK_PATH)
                .header(HttpHeaders.ACCEPT_LANGUAGE, EnumLanguageCode.ES.getCode())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(taskOperationDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorDetailsDto.class)
                .value(errorDetailsDTO -> {
                    Assertions.assertThat(errorDetailsDTO.getErrorCode())
                            .isEqualTo(EnumResourceError.TASK_NOT_FOUND.getCode());
                    Assertions.assertThat(errorDetailsDTO.getErrorMessage())
                            .isEqualTo("No se encontró la tarea con ID: " + taskOperationDto.getTask().getId() + ".");
                });
    }

    @Test
    @DisplayName("Delete not existing Task")
    void givenTaskId_whenDeleteTaskById_thenReturnError404() {
        taskOperationDto.setOperation(EnumTaskOperation.DELETE);
        taskOperationDto.getTask().setId(203L);
        this.webTestClient
                .post()
                .uri(TaskUtil.TASK_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(taskOperationDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorDetailsDto.class)
                .value(errorDetailsDTO -> {
                    Assertions.assertThat(errorDetailsDTO.getErrorCode())
                            .isEqualTo(EnumResourceError.TASK_NOT_FOUND.getCode());
                    Assertions.assertThat(errorDetailsDTO.getErrorMessage())
                            .isEqualTo("Task not found with ID: " + taskOperationDto.getTask().getId() + ".");
                });
    }

    @Test
    @DisplayName("Delete not existing Task - Spanish")
    void givenTaskId_whenDeleteTaskById_thenReturnError404InSpanish() {
        taskOperationDto.setOperation(EnumTaskOperation.DELETE);
        taskOperationDto.getTask().setId(204L);
        this.webTestClient
                .post()
                .uri(TaskUtil.TASK_PATH)
                .header(HttpHeaders.ACCEPT_LANGUAGE, EnumLanguageCode.ES.getCode())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(taskOperationDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorDetailsDto.class)
                .value(errorDetailsDTO -> {
                    Assertions.assertThat(errorDetailsDTO.getErrorCode())
                            .isEqualTo(EnumResourceError.TASK_NOT_FOUND.getCode());
                    Assertions.assertThat(errorDetailsDTO.getErrorMessage())
                            .isEqualTo("No se encontró la tarea con ID: " + taskOperationDto.getTask().getId() + ".");
                });
    }
}
