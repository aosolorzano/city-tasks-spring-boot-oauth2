package com.hiperium.city.tasks.api.common;

import com.hiperium.city.tasks.api.utils.ContainersUtil;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.lifecycle.Startables;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class AuthContainers extends BaseContainers {

    private static final KeycloakContainer KEYCLOAK_CONTAINER;

    private static AccessTokenResponse accessTokenResponse;
    private static CompletableFuture<Void> authContainers;

    protected static final String AUTHORIZATION = "Authorization";

    static {
        KEYCLOAK_CONTAINER = new KeycloakContainer()
                .withRealmImportFile("keycloak-realm.json");
        authContainers = Startables.deepStart(KEYCLOAK_CONTAINER);
    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        baseContainers.join();
        authContainers.join();
        initStorageContainers(registry);
        // SPRING SECURITY OAUTH2 JWT
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> KEYCLOAK_CONTAINER.getAuthServerUrl() + ContainersUtil.KEYCLOAK_REALM);
    }

    @BeforeAll
    public static void beforeAllTests() {
        Keycloak keycloakClient = KEYCLOAK_CONTAINER.getKeycloakAdminClient();
        accessTokenResponse = keycloakClient.tokenManager().getAccessToken();
    }

    protected String getBearerAccessToken() {
        return "Bearer " + Objects.requireNonNull(accessTokenResponse).getToken();
    }
}
