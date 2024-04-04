package com.sherryDev.taskmanagerapi.controller;

import com.sherryDev.taskmanagerapi.model.Task;
import com.sherryDev.taskmanagerapi.service.taskService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;


@RestController
@RequestMapping("/tasks-api")
public class TaskController {

    private final taskService service;
    @Autowired
    public TaskController(taskService service) {
        this.service = service;
    }

    @GetMapping("/find/{id}")
    private ResponseEntity<Task> findTaskById(@PathVariable int id) {
        Optional<Task> searchedTask = service.findTask(id);
        if(searchedTask.isPresent())
            return ResponseEntity.ok(searchedTask.get());

        return ResponseEntity.notFound().build();

    }

    @PostMapping("/create")
    private ResponseEntity<Void> createTask(@RequestBody Task newTask, UriComponentsBuilder ucb) {
        Task savedTask = service.saveTask(newTask);
        URI locationOfSavedTask = ucb.path("tasks-api/find/{id}").buildAndExpand(savedTask.getTId()).toUri();
        return ResponseEntity.created(locationOfSavedTask).build();
    }

	
}
