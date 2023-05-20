package com.hiperium.city.tasks.api.exception.handler;

import com.hiperium.city.tasks.api.dto.ErrorDetailsDto;
import com.hiperium.city.tasks.api.exception.ApplicationException;
import com.hiperium.city.tasks.api.exception.ResourceNotFoundException;
import com.hiperium.city.tasks.api.exception.TaskSchedulerException;
import com.hiperium.city.tasks.api.exception.ValidationException;
import com.hiperium.city.tasks.api.utils.ErrorUtil;
import com.hiperium.city.tasks.api.utils.enums.EnumValidationError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Value("${city.tasks.time.zone.id}")
    private String zoneId;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ValidationException.class)
    public final Mono<ResponseEntity<ErrorDetailsDto>> handleValidationException(
            ValidationException exception,
            ServerWebExchange exchange) {
        ErrorDetailsDto errorDetails = this.constructErrorDetailsDTO(exchange, exception);
        super.logger.error("handleValidationException(): " + errorDetails);
        return Mono.just(new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final Mono<ResponseEntity<ErrorDetailsDto>> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            ServerWebExchange exchange) {
        ErrorDetailsDto errorDetails = this.constructErrorDetailsDTO(exchange, exception);
        super.logger.error("handleResourceNotFoundException(): " + errorDetails);
        return Mono.just(new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(TaskSchedulerException.class)
    public final Mono<ResponseEntity<ErrorDetailsDto>> handleQuartzException(
            TaskSchedulerException exception,
            ServerWebExchange exchange) {
        ErrorDetailsDto errorDetails = this.constructErrorDetailsDTO(exchange, exception);
        super.logger.error("handleQuartzException(): " + errorDetails);
        return Mono.just(new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(ApplicationException.class)
    public final Mono<ResponseEntity<ErrorDetailsDto>> handleApplicationException(
            ApplicationException exception,
            ServerWebExchange exchange) {
        ErrorDetailsDto errorDetails = this.constructErrorDetailsDTO(exchange, exception);
        super.logger.error("handleApplicationException(): " + errorDetails);
        return Mono.just(new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST));
    }

    @Override
    public Mono<ResponseEntity<Object>> handleWebExchangeBindException(
            WebExchangeBindException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            ServerWebExchange exchange) {
        String errorMessage = null;
        FieldError fieldError = exception.getFieldError();
        if (Objects.nonNull(fieldError)) {
            String messageKey = fieldError.getDefaultMessage();
            errorMessage = this.getMessageFromProperties(messageKey, ErrorUtil.getLocale(exchange));
        }
        if (Objects.isNull(errorMessage)) {
            errorMessage = exception.getMessage();
        }
        ErrorDetailsDto errorDetails = ErrorUtil.getErrorDetailsVO(exchange, errorMessage,
                EnumValidationError.FIELD_VALIDATION_ERROR.getCode(), this.zoneId);
        super.logger.error("handleWebExchangeBindException(): " + errorDetails);
        return Mono.just(new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST));
    }

    private ErrorDetailsDto constructErrorDetailsDTO(ServerWebExchange exchange, ApplicationException exception) {
        String errorMessage = this.getMessageFromProperties(exception.getErrorMessageKey(),
                ErrorUtil.getLocale(exchange), exception.getArgs());
        return ErrorUtil.getErrorDetailsVO(exchange, errorMessage, exception.getErrorCode(), this.zoneId);
    }

    private String getMessageFromProperties(String messageKey, Locale locale, Object... args) {
        super.logger.debug("getMessageFromProperties() - Key: " + messageKey + " - Locale: " + locale);
        return this.messageSource.getMessage(messageKey, args, locale);
    }
}
