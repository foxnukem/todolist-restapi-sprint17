package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.Task;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskResponse {
    Long id;
    String name;
    String priority;
    Long todo_id;
    String state;

    public TaskResponse(Task task) {
        id = task.getId();
        name = task.getName();
        priority = task.getPriority().name();
        todo_id = task.getTodo().getId();
        state = task.getState().getName();
    }
}
