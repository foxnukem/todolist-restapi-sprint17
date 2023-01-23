package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.TaskRequest;
import com.softserve.itacademy.todolist.dto.TaskResponse;
import com.softserve.itacademy.todolist.model.Priority;
import com.softserve.itacademy.todolist.model.Task;
import com.softserve.itacademy.todolist.service.StateService;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TaskController {
    UserService userService;
    ToDoService toDoService;
    TaskService taskService;
    StateService stateService;

    @Autowired
    TaskController(UserService userService, ToDoService toDoService, TaskService taskService, StateService stateService) {
        this.userService = userService;
        this.toDoService = toDoService;
        this.taskService = taskService;
        this.stateService = stateService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or @toDoServiceImpl.readById(#t_id).owner.id==authentication.principal.id " +
            "or @toDoServiceImpl.readById(#t_id).collaborators.contains(@userServiceImpl.readById(authentication.principal.id))")
    @PostMapping("/todos/{t_id}/tasks")
    ResponseEntity<?> create(@PathVariable Long t_id, @RequestBody TaskRequest taskRequest) {
        Task newTask = new Task();
        newTask.setName(taskRequest.getName());
        newTask.setTodo(toDoService.readById(t_id));
        newTask.setState(stateService.getByName(taskRequest.getState()));
        newTask.setPriority(Priority.valueOf(taskRequest.getPriority()));
        return new ResponseEntity<>("Created Task " + new TaskResponse(taskService.create(newTask)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or @taskServiceImpl.readById(#task_id).todo.owner.id==authentication.principal.id " +
            "or @taskServiceImpl.readById(#task_id).todo.collaborators.contains(@userServiceImpl.readById(authentication.principal.id))")
    @GetMapping("/tasks/{task_id}")
    ResponseEntity<?> read(@PathVariable Long task_id) {
        return new ResponseEntity<>(new TaskResponse(taskService.readById(task_id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or @taskServiceImpl.readById(#task_id).todo.owner.id==authentication.principal.id " +
            "or @taskServiceImpl.readById(#task_id).todo.collaborators.contains(@userServiceImpl.readById(authentication.principal.id))")
    @PutMapping("/tasks/{task_id}")
    ResponseEntity<?> update(@PathVariable Long task_id, @RequestBody TaskRequest taskRequest) {
        Task toUpdate = taskService.readById(task_id);
        toUpdate.setName(taskRequest.getName());
        toUpdate.setState(stateService.getByName(taskRequest.getState()));
        toUpdate.setPriority(Priority.valueOf(taskRequest.getPriority()));
        return new ResponseEntity<>("Updated Task " + new TaskResponse(taskService.update(toUpdate)), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or @taskServiceImpl.readById(#task_id).todo.owner.id==authentication.principal.id " +
            "or @taskServiceImpl.readById(#task_id).todo.collaborators.contains(@userServiceImpl.readById(authentication.principal.id))")
    @DeleteMapping("/tasks/{task_id}")
    ResponseEntity<?> delete(@PathVariable Long task_id) {
        taskService.delete(task_id);
        return new ResponseEntity<>("Task with id " + task_id + " was deleted", HttpStatus.OK);
    }
}
