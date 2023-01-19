package com.softserve.itacademy.todolist.service;

import com.softserve.itacademy.todolist.model.State;

import java.util.List;

public interface StateService {
    State create(State state);
    State readById(long id);
    State update(State state);
    void delete(long id);
    List<State> getAll();

    State getByName(String name);
}
