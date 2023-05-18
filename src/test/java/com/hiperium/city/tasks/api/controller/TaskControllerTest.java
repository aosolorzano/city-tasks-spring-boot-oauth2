package com.hiperium.city.tasks.api.controller;

import com.hiperium.city.tasks.api.common.AuthContainers;
import com.hiperium.city.tasks.api.dto.ErrorDetailsDto;
import com.hiperium.city.tasks.api.dto.TaskDto;
import com.hiperium.city.tasks.api.dto.TaskOperationDto;
import com.hiperium.city.tasks.api.dto.TaskResponseDto;
import com.hiperium.city.tasks.api.utils.TaskUtil;
import com.hiperium.city.tasks.api.utils.enums.EnumDeviceOperation;
import com.hiperium.city.tasks.api.utils.enums.EnumResourceError;
import com.hiperium.city.tasks.api.utils.enums.EnumTaskOperation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@ActiveProfiles("test")
@TestInstance(PER_CLASS)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest extends AuthContainers {

    @Autowired
    private WebTestClient webTestClient;

    private static TaskOperationDto taskOperationDto;

    @BeforeAll
    public static void init() {
        taskOperationDto = TaskUtil.getTaskOperationTemplate();
    }

    @Test
    @Order(1)
    @DisplayName("Create Task")
    void givenTaskObject_whenSaveTask_thenReturnSavedTask() {
        taskOperationDto.setOperation(EnumTaskOperation.CREATE);
        this.webTestClient
                .post()
                .uri(TaskUtil.TASK_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, super.getBearerAccessToken())
                .bodyValue(taskOperationDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponseDto.class)
                .value(responseDto -> {
                    TaskDto createdTask = responseDto.getTask();
                    assertThat(createdTask.getId()).isNotNull().isPositive();
                    assertThat(createdTask.getName()).isEqualTo(taskOperationDto.getTask().getName());
                    assertThat(createdTask.getDescription()).isEqualTo(taskOperationDto.getTask().getDescription());
                    assertThat(createdTask.getHour()).isEqualTo(taskOperationDto.getTask().getHour());
                    assertThat(createdTask.getMinute()).isEqualTo(taskOperationDto.getTask().getMinute());
                    assertThat(createdTask.getExecutionDays()).isEqualTo(taskOperationDto.getTask().getExecutionDays());
                    assertThat(createdTask.getDeviceId()).isEqualTo(taskOperationDto.getTask().getDeviceId());
                    assertThat(createdTask.getDeviceOperation()).isEqualTo(taskOperationDto.getTask().getDeviceOperation());
                    taskOperationDto.getTask().setId(createdTask.getId());
                });
    }

    @Test
    @Order(2)
    @DisplayName("Update Task")
    void givenModifiedTask_whenUpdateTask_thenReturnUpdatedTask() {
        taskOperationDto.setOperation(EnumTaskOperation.UPDATE);
        taskOperationDto.getTask().setName("Test class updated");
        taskOperationDto.getTask().setDescription("Task description updated.");
        taskOperationDto.getTask().setHour(13);
        taskOperationDto.getTask().setMinute(30);
        taskOperationDto.getTask().setExecutionDays("MON,TUE,WED,THU,FRI,SAT,SUN");
        taskOperationDto.getTask().setDeviceOperation(EnumDeviceOperation.DEACTIVATE);
        this.webTestClient
                .post()
                .uri(TaskUtil.TASK_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, super.getBearerAccessToken())
                .bodyValue(taskOperationDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponseDto.class)
                .value(responseDto -> {
                    TaskDto updatedTask = responseDto.getTask();
                    assertThat(updatedTask.getId()).isEqualTo(taskOperationDto.getTask().getId());
                    assertThat(updatedTask.getName()).isEqualTo(taskOperationDto.getTask().getName());
                    assertThat(updatedTask.getDescription()).isEqualTo(taskOperationDto.getTask().getDescription());
                    assertThat(updatedTask.getHour()).isEqualTo(taskOperationDto.getTask().getHour());
                    assertThat(updatedTask.getMinute()).isEqualTo(taskOperationDto.getTask().getMinute());
                    assertThat(updatedTask.getExecutionDays()).isEqualTo(taskOperationDto.getTask().getExecutionDays());
                    assertThat(updatedTask.getDeviceOperation()).isEqualTo(taskOperationDto.getTask().getDeviceOperation());
                });
    }

    @Test
    @Order(3)
    @DisplayName("Delete Task")
    void givenTaskObject_whenDeleteTask_thenReturnResponse200() {
        taskOperationDto.setOperation(EnumTaskOperation.DELETE);
        this.webTestClient
                .post()
                .uri(TaskUtil.TASK_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, super.getBearerAccessToken())
                .bodyValue(taskOperationDto)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(4)
    @DisplayName("Delete not existing Task")
    void givenTaskId_whenDeleteTaskById_thenReturnError404() {
        taskOperationDto.setOperation(EnumTaskOperation.DELETE);
        this.webTestClient
                .post()
                .uri(TaskUtil.TASK_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, super.getBearerAccessToken())
                .bodyValue(taskOperationDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorDetailsDto.class)
                .value(errorDetailsDTO -> Assertions.assertThat(errorDetailsDTO.getErrorCode())
                        .isEqualTo(EnumResourceError.TASK_NOT_FOUND.getCode()));
    }
}
