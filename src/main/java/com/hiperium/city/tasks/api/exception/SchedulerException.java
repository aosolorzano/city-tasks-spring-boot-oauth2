package com.hiperium.city.tasks.api.exception;

import com.hiperium.city.tasks.api.utils.enums.EnumSchedulerError;

public class SchedulerException extends ApplicationException {

    public SchedulerException(org.quartz.SchedulerException e, EnumSchedulerError errorEnum, Object... args) {
        super(e, errorEnum.getCode(), errorEnum.getMessage(), args);
    }
}
