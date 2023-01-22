package com.softserve.itacademy.todolist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.model.ToDo;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ToDoResponse {
    Long id;
    String title;
    LocalDateTime createdAt;
    UserResponse owner;
    List<TaskResponse> tasks;
    List<UserResponse> collaborators;

    public ToDoResponse(ToDo toDo) {
        id = toDo.getId();
        title = toDo.getTitle();
        createdAt = toDo.getCreatedAt();
        owner = new UserResponse(toDo.getOwner());
        tasks = toDo.getTasks().stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());
        collaborators = toDo.getCollaborators().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
}
