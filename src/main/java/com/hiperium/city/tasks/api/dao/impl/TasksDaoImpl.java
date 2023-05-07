package com.hiperium.city.tasks.api.dao.impl;

import com.hiperium.city.tasks.api.dao.TasksDao;
import com.hiperium.city.tasks.api.dto.TaskCriteriaDto;
import com.hiperium.city.tasks.api.dto.TaskDto;
import com.hiperium.city.tasks.api.exception.ValidationException;
import com.hiperium.city.tasks.api.model.Task;
import com.hiperium.city.tasks.api.utils.enums.EnumValidationError;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class TasksDaoImpl implements TasksDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TasksDaoImpl.class);

    private final EntityManager entityManager;

    public TasksDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<TaskDto> find(TaskCriteriaDto criteriaDto) {
        LOGGER.debug("find() - START: {}", criteriaDto);
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskDto> cq = cb.createQuery(TaskDto.class);
        Root<Task> taskRoot = cq.from(Task.class);
        cq.select(getTaskDtoConstruct(cb, taskRoot));

        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(criteriaDto.getId()) && criteriaDto.getId() >= 0L) {
            predicates.add(cb.equal(taskRoot.get("id"), criteriaDto.getId()));
        }
        if (Objects.nonNull(criteriaDto.getName()) && !criteriaDto.getName().isBlank()) {
            predicates.add(cb.like(taskRoot.get("name"), "%" + criteriaDto.getName() + "%"));
        }
        if (Objects.nonNull(criteriaDto.getStatus())) {
            predicates.add(cb.equal(taskRoot.get("status"), criteriaDto.getStatus()));
        }
        if (Objects.nonNull(criteriaDto.getDeviceId()) && !criteriaDto.getDeviceId().isBlank()) {
            predicates.add(cb.equal(taskRoot.get("deviceId"), criteriaDto.getDeviceId()));
        }
        if (Objects.nonNull(criteriaDto.getDeviceOperation())) {
            predicates.add(cb.equal(taskRoot.get("deviceOperation"), criteriaDto.getDeviceOperation()));
        }
        if (Objects.nonNull(criteriaDto.getExecutionDay()) && !criteriaDto.getExecutionDay().isBlank()) {
            predicates.add(cb.like(taskRoot.get("executionDays"), "%" + criteriaDto.getExecutionDay() + "%"));
        }
        if (Objects.nonNull(criteriaDto.getHour()) && criteriaDto.getHour() >= 0 && criteriaDto.getHour() <= 23) {
            predicates.add(cb.equal(taskRoot.get("hour"), criteriaDto.getHour()));
        }
        if (Objects.nonNull(criteriaDto.getMinute()) && criteriaDto.getMinute() >= 0 && criteriaDto.getMinute() <= 59) {
            predicates.add(cb.equal(taskRoot.get("minute"), criteriaDto.getMinute()));
        }
        assignQueryPredicates(cq, predicates);
        return this.entityManager.createQuery(cq).getResultList();
    }

    private static void assignQueryPredicates(CriteriaQuery<TaskDto> cq, List<Predicate> predicates) {
        if (predicates.isEmpty()) {
            throw new ValidationException(EnumValidationError.NO_CRITERIA_FOUND);
        } else if (predicates.size() == 1) {
            cq.where(predicates.get(0));
        } else {
            cq.where(predicates.toArray(new Predicate[0]));
        }
    }

    private static CompoundSelection<TaskDto> getTaskDtoConstruct(CriteriaBuilder cb, Root<Task> taskRoot) {
        return cb.construct(TaskDto.class, taskRoot.get("id"), taskRoot.get("name"),
                taskRoot.get("description"), taskRoot.get("status"), taskRoot.get("deviceId"),
                taskRoot.get("deviceOperation"), taskRoot.get("hour"), taskRoot.get("minute"),
                taskRoot.get("executionDays"), taskRoot.get("executeUntil"));
    }
}
