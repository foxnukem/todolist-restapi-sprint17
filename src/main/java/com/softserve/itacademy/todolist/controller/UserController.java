package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.TaskResponse;
import com.softserve.itacademy.todolist.dto.ToDoResponse;
import com.softserve.itacademy.todolist.dto.UserResponse;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    List<UserResponse> getAll() {
        return userService.getAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{u_id}/todos")
    ResponseEntity<?> getTodos(@PathVariable Long u_id) {
        return new ResponseEntity<>(
                userService.readById(u_id).getMyTodos().stream()
                        .map(ToDoResponse::new)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/{u_id}/todos/{t_id}/collaborators")
    ResponseEntity<?> getTodoCollaborators(@PathVariable Long u_id, @PathVariable Long t_id) {
        return new ResponseEntity<>(
                userService.readById(u_id).getMyTodos().stream()
                        .filter(e -> e.getId().equals(t_id))
                        .findFirst()
                        .orElseThrow()
                        .getCollaborators().stream()
                        .map(UserResponse::new)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/{u_id}/todos/{t_id}/tasks")
    ResponseEntity<?> getTasksOfTheToDo(@PathVariable Long u_id, @PathVariable Long t_id) {
        return new ResponseEntity<>(userService.readById(u_id).getMyTodos().stream()
                .filter(e -> e.getId().equals(t_id))
                .findFirst()
                .orElseThrow()
                .getTasks().stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
