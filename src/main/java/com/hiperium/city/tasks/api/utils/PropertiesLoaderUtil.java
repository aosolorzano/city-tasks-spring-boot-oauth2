package com.hiperium.city.tasks.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hiperium.city.tasks.api.vo.AuroraSecretsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Objects;

public final class PropertiesLoaderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoaderUtil.class);
    private static final String JDBC_SQL_CONNECTION = "jdbc:postgresql://{0}:{1}/{2}";

    private PropertiesLoaderUtil() {
        // Private constructor.
    }

    public static void setJdbcConnection() throws JsonProcessingException {
        AuroraSecretsVo auroraSecretVO = EnvironmentUtil.getAuroraSecretVO();
        if (Objects.nonNull(auroraSecretVO)) {
            String sqlConnection = MessageFormat.format(JDBC_SQL_CONNECTION, auroraSecretVO.host(),
                    auroraSecretVO.port(), auroraSecretVO.dbname());
            LOGGER.info("JDBC Connection found: {}", sqlConnection);
            // Set JDBC connection for JPA.
            System.setProperty("spring.datasource.url", sqlConnection);
            System.setProperty("spring.datasource.username", auroraSecretVO.username());
            System.setProperty("spring.datasource.password", auroraSecretVO.password());
            // Set JDBC connection for Quartz.
            System.setProperty("spring.quartz.properties.org.quartz.dataSource.cityTasksQuartzDS.URL", sqlConnection);
            System.setProperty("spring.quartz.properties.org.quartz.dataSource.cityTasksQuartzDS.user", auroraSecretVO.username());
            System.setProperty("spring.quartz.properties.org.quartz.dataSource.cityTasksQuartzDS.password", auroraSecretVO.password());
        }
    }

    public static void setIdpServiceEndpoint() {
        String idpEndpoint = EnvironmentUtil.getIdpEndpoint();
        if (Objects.nonNull(idpEndpoint)) {
            LOGGER.info("IdP URI: {}", idpEndpoint);
            System.setProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri", idpEndpoint);
        }
    }

    public static void setApplicationTimeZone() {
        String timeZoneId = EnvironmentUtil.getTimeZoneId();
        if (Objects.nonNull(timeZoneId)) {
            LOGGER.info("Time Zone ID found: {}", timeZoneId);
            System.setProperty("city.tasks.time.zone.id", timeZoneId);
        }
    }

    public static void setAwsCredentials() {
        String awsAccessKey = EnvironmentUtil.getAwsAccessKey();
        if (Objects.nonNull(awsAccessKey)) {
            LOGGER.info("AWS Access Key found: {}", awsAccessKey);
            System.setProperty("aws.accessKeyId", awsAccessKey);
        }
        String awsSecretKey = EnvironmentUtil.getAwsSecretKey();
        if (Objects.nonNull(awsSecretKey)) {
            LOGGER.info("AWS Secret Key found: {}", awsSecretKey);
            System.setProperty("aws.secretKey", awsSecretKey);
        }
        String awsRegion = EnvironmentUtil.getAwsRegion();
        if (Objects.nonNull(awsRegion)) {
            LOGGER.info("AWS Region found: {}", awsRegion);
            System.setProperty("aws.region", awsRegion);
        }
    }

    public static void setAwsEndpointOverride() {
        String endpointOverride = EnvironmentUtil.getAwsEndpointOverride();
        if (Objects.nonNull(endpointOverride)) {
            LOGGER.info("AWS Endpoint-Override found: {}", endpointOverride);
            System.setProperty("aws.dynamodb.endpoint-override", endpointOverride);
        }
    }
}
