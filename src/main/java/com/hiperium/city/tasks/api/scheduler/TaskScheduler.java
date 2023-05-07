package com.hiperium.city.tasks.api.scheduler;

import com.hiperium.city.tasks.api.model.Task;

public interface TaskScheduler {

    void scheduleJob(final Task task);
    void rescheduleJob(final Task task);
    void unscheduleJob(final Task task);
}
