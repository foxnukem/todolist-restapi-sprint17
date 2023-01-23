package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.ToDoResponse;
import com.softserve.itacademy.todolist.dto.UserRequest;
import com.softserve.itacademy.todolist.dto.UserResponse;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.RoleService;
import com.softserve.itacademy.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    UserService userService;
    RoleService roleService;
    PasswordEncoder passwordEncoder;

    @Autowired
    UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    List<UserResponse> getAll() {
        return userService.getAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/signup")
    ResponseEntity<?> create(@RequestBody UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(roleService.readById(2));
        return new ResponseEntity<>("Created User " + new UserResponse(userService.create(user)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id==authentication.principal.id")
    @GetMapping("/{id}")
    ResponseEntity<?> read(@PathVariable Long id) {
        return new ResponseEntity<>(new UserResponse(userService.readById(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id==authentication.principal.id")
    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        User oldUser = userService.readById(id);
        User user = new User();
        user.setId(oldUser.getId());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(oldUser.getRole());
        return new ResponseEntity<>("Updated User " + userService.update(user), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>("User with id " + id + " was deleted", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #u_id==authentication.principal.id")
    @GetMapping("/{u_id}/todos")
    ResponseEntity<?> getTodos(@PathVariable Long u_id) {
        return new ResponseEntity<>(
                userService.readById(u_id).getMyTodos().stream()
                        .map(ToDoResponse::new)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
