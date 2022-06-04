package com.mk.controller;

import com.mk.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @GetMapping(value = "/")
    public ResponseEntity<?> get() {
        return ok("Welcome to the M.K's Simple To Do Application !!");

    }

    @GetMapping(value = "/list")
    public ResponseEntity<?> getList() {
        return ok(toDoService.findAllList());
    }
}
