package com.hiperium.city.tasks.api.controller;

import com.hiperium.city.tasks.api.common.AbstractContainerBaseTest;
import com.hiperium.city.tasks.api.dto.ErrorDetailsDto;
import com.hiperium.city.tasks.api.dto.TaskCriteriaDto;
import com.hiperium.city.tasks.api.utils.TaskUtil;
import com.hiperium.city.tasks.api.utils.enums.EnumLanguageCode;
import com.hiperium.city.tasks.api.utils.enums.EnumValidationError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@ActiveProfiles("test")
@TestInstance(PER_CLASS)
@AutoConfigureWebTestClient
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TasksControllerExceptionsTest extends AbstractContainerBaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Find Tasks with empty criteria")
    void givenEmptyCriteria_whenFindTasks_thenReturnError() {
        TaskCriteriaDto taskCriteriaDto = new TaskCriteriaDto();
        this.webTestClient
                .post()
                .uri(TaskUtil.TASKS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, super.getBearerAccessToken())
                .bodyValue(taskCriteriaDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDetailsDto.class)
                .value(errorDetailsDTO -> {
                    Assertions.assertThat(errorDetailsDTO.getErrorCode())
                            .isEqualTo(EnumValidationError.NO_CRITERIA_FOUND.getCode());
                    Assertions.assertThat(errorDetailsDTO.getErrorMessage())
                            .isEqualTo("Search params cannot be null.");
                });
    }

    @Test
    @DisplayName("Find Tasks with empty criteria - Spanish")
    void givenEmptyCriteria_whenFindTasks_thenReturnErrorInSpanish() {
        TaskCriteriaDto taskCriteriaDto = new TaskCriteriaDto();
        this.webTestClient
                .post()
                .uri(TaskUtil.TASKS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, EnumLanguageCode.ES.getCode())
                .header(AUTHORIZATION, super.getBearerAccessToken())
                .bodyValue(taskCriteriaDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDetailsDto.class)
                .value(errorDetailsDTO -> {
                    Assertions.assertThat(errorDetailsDTO.getErrorCode())
                            .isEqualTo(EnumValidationError.NO_CRITERIA_FOUND.getCode());
                    Assertions.assertThat(errorDetailsDTO.getErrorMessage())
                            .isEqualTo("Los parámetros de busqueda no pueden ser nulos.");
                });
    }
}
