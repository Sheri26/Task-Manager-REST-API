package com.sherryDev.taskmanagerapi.controller;

import com.sherryDev.taskmanagerapi.model.Task;
import com.sherryDev.taskmanagerapi.service.taskService;
import org.apache.catalina.connector.Response;
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


    @GetMapping("/findAll")
    private ResponseEntity<Iterable<Task>> getAllTasks() {
        if ((service.getTotalSavedEntities()) > 0) {
            Iterable<Task> listOfTasks = service.findAllTasks();
            return ResponseEntity.ok(listOfTasks);
        }
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/amend/{id}")
    private ResponseEntity<String> updateTaskById(@PathVariable("id") int updId, @RequestBody Task task) {
        if (service.checkExistenceById(updId)) {
            Task toBeUpd = service.findTask(updId).get();
            if (!((task.getTName().isBlank()))) {
                toBeUpd.setTName(task.getTName());
            }
            if (!((task.getTDescription()).isBlank())) {
                toBeUpd.setTDescription(task.getTDescription());
            }
//            toBeUpd = task;
            service.saveTask(toBeUpd);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/remove")
    private ResponseEntity<String> deleteTaskById(@RequestParam int id) {
        if (service.checkExistenceById(id)) {
            service.removeTask(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
