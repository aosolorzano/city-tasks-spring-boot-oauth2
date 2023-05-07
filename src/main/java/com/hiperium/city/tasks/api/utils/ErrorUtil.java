package com.hiperium.city.tasks.api.utils;

import com.hiperium.city.tasks.api.dto.ErrorDetailsDto;
import org.springframework.web.server.ServerWebExchange;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public final class ErrorUtil {

    private ErrorUtil() {
        // Empty constructor.
    }

    public static ErrorDetailsDto getErrorDetailsVO(ServerWebExchange exchange,
                                                    String errorMessage,
                                                    String errorCode,
                                                    String zoneId) {
        return ErrorDetailsDto.builder()
                .errorDate(ZonedDateTime.now(TimeZone.getTimeZone(ZoneId.of(zoneId)).toZoneId()))
                .requestedPath(exchange.getRequest().getPath().toString())
                .errorMessage(errorMessage)
                .errorCode(errorCode)
                .build();
    }

    public static Locale getLocale(ServerWebExchange exchange) {
        List<Locale> localeList = exchange.getRequest().getHeaders().getAcceptLanguageAsLocales();
        if (localeList.isEmpty()) {
            return new Locale("en", "US");
        }
        return localeList.get(0);
    }
}
