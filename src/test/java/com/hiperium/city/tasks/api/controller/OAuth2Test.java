package com.hiperium.city.tasks.api.controller;

import com.hiperium.city.tasks.api.common.AbstractContainerBaseTest;
import com.hiperium.city.tasks.api.dto.TaskOperationDto;
import com.hiperium.city.tasks.api.utils.TaskUtil;
import com.hiperium.city.tasks.api.utils.enums.EnumTaskOperation;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@ActiveProfiles("test")
@TestInstance(PER_CLASS)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OAuth2Test extends AbstractContainerBaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    @DisplayName("Create Task without Authorization token")
    void givenTaskObject_whenSaveTask_thenReturnUnauthorized() {
        TaskOperationDto taskOperationDto = TaskUtil.getTaskOperationTemplate();
        taskOperationDto.setOperation(EnumTaskOperation.CREATE);
        this.webTestClient
                .post()
                .uri(TaskUtil.TASK_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(taskOperationDto)
                .exchange()
                .expectStatus().isForbidden();  // Because the security control for CSRF attacks enable by default.
    }

    @Test
    @Order(2)
    @DisplayName("Find all Tasks without Authorization token")
    void givenTasksList_whenFindAllTasks_thenReturnUnauthorized() {
        this.webTestClient
                .get()
                .uri(TaskUtil.TASKS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
