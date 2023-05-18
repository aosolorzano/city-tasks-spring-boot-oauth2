package com.hiperium.city.tasks.api.common;

import com.hiperium.city.tasks.api.utils.ContainersUtil;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.CompletableFuture;

public class BaseContainers {

    protected static PostgreSQLContainer<?> POSTGRES_CONTAINER;
    protected static LocalStackContainer LOCALSTACK_CONTAINER;
    protected static CompletableFuture<Void> baseContainers;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:14.4")
                .withUsername("postgres")
                .withPassword("postgres123")
                .withDatabaseName("HiperiumCityTasksDB");
        LOCALSTACK_CONTAINER = new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
                .withServices(LocalStackContainer.Service.DYNAMODB);
        baseContainers = Startables.deepStart(POSTGRES_CONTAINER, LOCALSTACK_CONTAINER);
    }

    protected static void initStorageContainers(DynamicPropertyRegistry registry) {
        // SPRING DATA JDBC CONNECTION
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name",
                () -> ContainersUtil.POSTGRESQL_DRIVER);
        // SPRING QUARTZ JDBC CONNECTION
        registry.add("spring.quartz.properties.org.quartz.dataSource.cityTasksQuartzDS.URL",
                POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.quartz.properties.org.quartz.dataSource.cityTasksQuartzDS.user",
                POSTGRES_CONTAINER::getUsername);
        registry.add("spring.quartz.properties.org.quartz.dataSource.cityTasksQuartzDS.password",
                POSTGRES_CONTAINER::getPassword);
        registry.add("spring.quartz.properties.org.quartz.dataSource.cityTasksQuartzDS.driver",
                () -> ContainersUtil.POSTGRESQL_DRIVER);
        registry.add("spring.quartz.properties.org.quartz.dataSource.cityTasksQuartzDS.provider",
                () -> ContainersUtil.QUARTZ_DS_PROVIDER);
        // AWS DYNAMODB LOCALSTACK CONNECTION
        registry.add("aws.region", LOCALSTACK_CONTAINER::getRegion);
        registry.add("aws.accessKeyId", LOCALSTACK_CONTAINER::getAccessKey);
        registry.add("aws.secretAccessKey", LOCALSTACK_CONTAINER::getSecretKey);
        registry.add("aws.endpoint-override",
                () -> LOCALSTACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString());
    }
}
