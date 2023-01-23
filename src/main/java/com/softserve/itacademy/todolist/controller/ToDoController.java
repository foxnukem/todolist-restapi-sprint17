package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.*;
import com.softserve.itacademy.todolist.model.ToDo;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.TaskService;
import com.softserve.itacademy.todolist.service.ToDoService;
import com.softserve.itacademy.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ToDoController {
    UserService userService;
    ToDoService toDoService;
    TaskService taskService;

    @Autowired
    ToDoController(UserService userService, ToDoService toDoService, TaskService taskService) {
        this.userService = userService;
        this.toDoService = toDoService;
        this.taskService = taskService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or @toDoServiceImpl.readById(#t_id).owner.id==authentication.principal.id " +
            "or @toDoServiceImpl.readById(#t_id).collaborators.contains(@userServiceImpl.readById(authentication.principal.id))")
    @GetMapping("/todos/{t_id}/collaborators")
    ResponseEntity<?> getTodoCollaborators(@PathVariable Long t_id) {
        return new ResponseEntity<>(
                toDoService.readById(t_id).getCollaborators().stream().map(CollaboratorResponse::new).toList(),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #u_id==authentication.principal.id")
    @PostMapping("/users/{u_id}/todos")
    ResponseEntity<?> create(@PathVariable Long u_id, @RequestBody ToDoRequest toDoRequest) {
        ToDo newToDo = new ToDo();
        newToDo.setTitle(toDoRequest.getTitle());
        newToDo.setCreatedAt(LocalDateTime.now());
        newToDo.setOwner(userService.readById(u_id));
        return new ResponseEntity<>("Created ToDo " + new ToDoResponse(toDoService.create(newToDo)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or @toDoServiceImpl.readById(#t_id).owner.id==authentication.principal.id " +
            "or @toDoServiceImpl.readById(#t_id).collaborators.contains(@userServiceImpl.readById(authentication.principal.id))")
    @GetMapping("/todos/{t_id}")
    ResponseEntity<?> read(@PathVariable Long t_id) {
        return new ResponseEntity<>(new ToDoResponse(toDoService.readById(t_id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @toDoServiceImpl.readById(#t_id).owner.id==authentication.principal.id")
    @PutMapping("/todos/{t_id}")
    ResponseEntity<?> update(@PathVariable Long t_id, @RequestBody ToDoRequest toDoRequest) {
        ToDo toUpdate = toDoService.readById(t_id);
        toUpdate.setTitle(toDoRequest.getTitle());
        return new ResponseEntity<>("Updated ToDo " + new ToDoResponse(toDoService.update(toUpdate)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @toDoServiceImpl.readById(#t_id).owner.id==authentication.principal.id")
    @DeleteMapping("/todos/{t_id}")
    ResponseEntity<?> delete(@PathVariable Long t_id) {
        toDoService.delete(t_id);
        return new ResponseEntity<>("ToDo with id " + t_id + " was deleted", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') " +
            "or @toDoServiceImpl.readById(#t_id).owner.id==authentication.principal.id " +
            "or @toDoServiceImpl.readById(#t_id).collaborators.contains(@userServiceImpl.readById(authentication.principal.id))")
    @GetMapping("/todos/{t_id}/tasks")
    ResponseEntity<?> getTasksOfTheToDo(@PathVariable Long t_id) {
        return new ResponseEntity<>(toDoService.readById(t_id)
                .getTasks().stream()
                .map(TaskResponse::new)
                .toList(),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @toDoServiceImpl.readById(#t_id).owner.id==authentication.principal.id")
    @PostMapping("/todos/{t_id}/collaborators")
    ResponseEntity<?> addCollaborator(@PathVariable Long t_id, @RequestBody CollaboratorRequest collaboratorRequest) {
        User newCollaborator = userService.readById(collaboratorRequest.getCollaboratorId());
        ToDo todo = toDoService.readById(t_id);
        List<User> collaborators = todo.getCollaborators();
        if (collaborators.contains(newCollaborator)) {
            return new ResponseEntity<>("User (id=" + collaboratorRequest.getCollaboratorId() + ") is already collaborator on ToDo(id=" + t_id + ")", HttpStatus.CONFLICT);
        }
        collaborators.add(newCollaborator);
        todo.setCollaborators(collaborators);
        toDoService.update(todo);
        return new ResponseEntity<>("User (id=" + collaboratorRequest.getCollaboratorId() + ") added as collaborator to ToDo(id=" + t_id + ")", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @toDoServiceImpl.readById(#t_id).owner.id==authentication.principal.id")
    @DeleteMapping("/todos/{t_id}/collaborators/{c_id}")
    ResponseEntity<?> removeCollaborator(@PathVariable Long t_id, @PathVariable Long c_id) {
        ToDo todo = toDoService.readById(t_id);
        List<User> collaborators = todo.getCollaborators();
        collaborators.remove(userService.readById(c_id));
        todo.setCollaborators(collaborators);
        toDoService.update(todo);
        return new ResponseEntity<>("User (id=" + c_id + ") removed from ToDo(id=" + t_id + ")", HttpStatus.OK);
    }
}
