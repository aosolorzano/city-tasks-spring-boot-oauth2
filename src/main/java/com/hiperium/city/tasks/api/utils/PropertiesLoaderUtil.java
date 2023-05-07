package com.hiperium.city.tasks.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hiperium.city.tasks.api.vo.AuroraPostgresSecretVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Objects;

public final class PropertiesLoaderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoaderUtil.class);
    private static final String JDBC_SQL_CONNECTION = "jdbc:postgresql://{0}:{1}/{2}";
    public static final String POSTGRESQL_DRIVER_NAME = "org.postgresql.Driver";

    private PropertiesLoaderUtil() {
        // Private constructor.
    }

    public static void settingJdbcConnection() throws JsonProcessingException {
        AuroraPostgresSecretVo auroraSecretVO = EnvironmentUtil.getAuroraSecretVO();
        if (Objects.nonNull(auroraSecretVO)) {
            String sqlConnection = MessageFormat.format(JDBC_SQL_CONNECTION, auroraSecretVO.host(),
                    auroraSecretVO.port(), auroraSecretVO.dbname());
            LOGGER.info("JDBC Connection found: {}", sqlConnection);
            // Set the default JDBC connection.
            System.setProperty("spring.datasource.url", sqlConnection);
            System.setProperty("spring.datasource.username", auroraSecretVO.username());
            System.setProperty("spring.datasource.password", auroraSecretVO.password());
            System.setProperty("spring.datasource.driver-class-name", POSTGRESQL_DRIVER_NAME);
            // Set the default JDBC connection for Quartz.
            System.setProperty("spring.scheduler.properties.org.scheduler.dataSource.CityTasksQuartzDS.URL", sqlConnection);
            System.setProperty("spring.scheduler.properties.org.scheduler.dataSource.CityTasksQuartzDS.user", auroraSecretVO.username());
            System.setProperty("spring.scheduler.properties.org.scheduler.dataSource.CityTasksQuartzDS.password", auroraSecretVO.password());
            System.setProperty("spring.scheduler.properties.org.scheduler.dataSource.CityTasksQuartzDS.driver", POSTGRESQL_DRIVER_NAME);
            System.setProperty("spring.scheduler.properties.org.scheduler.dataSource.CityTasksQuartzDS.provider", "hikaricp");
        }
    }

    public static void settingDefaultTimeZone() {
        String timeZoneId = EnvironmentUtil.getTimeZoneId();
        if (Objects.nonNull(timeZoneId)) {
            LOGGER.info("Time Zone ID found: {}", timeZoneId);
            System.setProperty("hiperium.city.tasks.time.zone.id", timeZoneId);
        }
    }

    public static void settingAwsRegion() {
        String awsRegion = EnvironmentUtil.getAwsRegion();
        if (Objects.nonNull(awsRegion)) {
            LOGGER.info("AWS Region found: {}", awsRegion);
            System.setProperty("aws.region", awsRegion);
        }
    }

    public static void settingAwsAccessKey() {
        String awsAccessKey = EnvironmentUtil.getAwsAccessKey();
        if (Objects.nonNull(awsAccessKey)) {
            LOGGER.info("AWS Access Key found: {}", awsAccessKey);
            System.setProperty("aws.accessKeyId", awsAccessKey);
        }
    }

    public static void settingAwsSecretKey() {
        String awsSecretKey = EnvironmentUtil.getAwsSecretKey();
        if (Objects.nonNull(awsSecretKey)) {
            LOGGER.info("AWS Secret Key found: {}", awsSecretKey);
            System.setProperty("aws.secretKey", awsSecretKey);
        }
    }

    public static void settingAwsEndpointOverride() {
        String endpointOverride = EnvironmentUtil.getAwsEndpointOverride();
        if (Objects.nonNull(endpointOverride)) {
            LOGGER.info("AWS Endpoint-Override found: {}", endpointOverride);
            System.setProperty("aws.dynamodb.endpoint-override", endpointOverride);
        }
    }
}
