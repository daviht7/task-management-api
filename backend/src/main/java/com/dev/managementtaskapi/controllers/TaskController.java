package com.dev.managementtaskapi.controllers;

import com.dev.managementtaskapi.dtos.ChangeTaskIntoSubTaskDto;
import com.dev.managementtaskapi.dtos.CreateTaskDto;
import com.dev.managementtaskapi.models.TaskModel;
import com.dev.managementtaskapi.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskModel> addTask(@Valid @RequestBody CreateTaskDto createTaskDto) {

        var taskModel = new TaskModel();
        BeanUtils.copyProperties(createTaskDto, taskModel);

        var taskModelInserted = taskService.createTask(taskModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskModelInserted);
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> findAll() {
        var tasks = taskService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("{taskId}")
    public ResponseEntity<TaskModel> findById(@PathVariable UUID taskId) {
        var task = taskService.findById(taskId);

        return ResponseEntity.status(HttpStatus.OK).body(task);

    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> deleteById(@PathVariable UUID taskId) {
        var task = taskService.findById(taskId);

        taskService.deleteTask(task);
        return ResponseEntity.ok().build();

    }

    @PutMapping("{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable UUID taskId, @RequestBody CreateTaskDto createTaskDto) {

        taskService.updateTask(taskId, createTaskDto);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("{taskId}/change-task-into-sub-task")
    public ResponseEntity<?> changeTaskIntoSubTask(@PathVariable UUID taskId, @RequestBody ChangeTaskIntoSubTaskDto changeTaskIntoSubTaskDto) {

        taskService.changeTaskIntoSubTask(taskId, changeTaskIntoSubTaskDto);
        return ResponseEntity.noContent().build();

    }

}
