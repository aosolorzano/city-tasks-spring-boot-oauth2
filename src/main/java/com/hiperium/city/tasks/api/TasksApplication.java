package com.hiperium.city.tasks.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hiperium.city.tasks.api.config.hints.ResourceBundleHints;
import com.hiperium.city.tasks.api.config.hints.PostgresHints;
import com.hiperium.city.tasks.api.config.hints.QuartzHints;
import com.hiperium.city.tasks.api.dto.ErrorDetailsDto;
import com.hiperium.city.tasks.api.scheduler.execution.JobExecution;
import com.hiperium.city.tasks.api.utils.PropertiesLoaderUtil;
import com.hiperium.city.tasks.api.vo.AuroraSecretsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
@ImportRuntimeHints({PostgresHints.class, QuartzHints.class, ResourceBundleHints.class})
@RegisterReflectionForBinding({AuroraSecretsVo.class, JobExecution.class, ErrorDetailsDto.class})
public class TasksApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TasksApplication.class);

    public static void main(String[] args) throws JsonProcessingException {
        LOGGER.info("main() - BEGIN");
        setApplicationProperties();
        SpringApplication.run(TasksApplication.class, args);
        LOGGER.info("main() - END");
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding(StandardCharsets.ISO_8859_1.name());
        return messageSource;
    }

    private static void setApplicationProperties() throws JsonProcessingException {
        PropertiesLoaderUtil.setJdbcConnection();
        PropertiesLoaderUtil.setIdpServiceEndpoint();
        PropertiesLoaderUtil.setApplicationTimeZone();
        PropertiesLoaderUtil.setAwsCredentials();
        PropertiesLoaderUtil.setAwsEndpointOverride();
    }
}
