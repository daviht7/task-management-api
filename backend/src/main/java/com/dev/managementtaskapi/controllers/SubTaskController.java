package com.dev.managementtaskapi.controllers;

import com.dev.managementtaskapi.dtos.CreateSubTaskDto;
import com.dev.managementtaskapi.models.SubTaskModel;
import com.dev.managementtaskapi.services.SubTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping
public class SubTaskController {

    @Autowired
    private SubTaskService subTaskService;

    @PostMapping("task/{taskId}/sub-task")
    public ResponseEntity<SubTaskModel> addSubTask(@PathVariable("taskId") UUID taskId, @RequestBody CreateSubTaskDto createSubTaskDto) {
        var subTaskModel = subTaskService.createSubTask(taskId, createSubTaskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(subTaskModel);
    }

    @PutMapping("task/{taskId}/sub-task/{subTaskId}")
    public ResponseEntity<?> updateSubTask(@PathVariable("taskId") UUID taskId, @PathVariable("subTaskId") UUID subTaskId, @RequestBody CreateSubTaskDto createSubTaskDto) {
        subTaskService.updateSubTask(taskId, subTaskId, createSubTaskDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("task/{taskId}/sub-task/{subTaskId}")
    public ResponseEntity<?> deleteSubTask(@PathVariable("taskId") UUID taskId, @PathVariable("subTaskId") UUID subTaskId) {
        subTaskService.deleteSubTask(taskId, subTaskId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("sub-task/{subTaskId}/change-sub-task-into-task")
    public ResponseEntity<?> changeSubTaskIntoTask(@PathVariable UUID subTaskId) {
        subTaskService.changeSubTaskIntoTask(subTaskId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("sub-task/{subTaskId}/task/{taskId}/change-task-from-sub-task")
    public ResponseEntity<?> changeTaskFromSubTask(@PathVariable("subTaskId") UUID subTaskId, @PathVariable("taskId") UUID taskId) {
        subTaskService.changeTaskFromSubTask(subTaskId, taskId);
        return ResponseEntity.noContent().build();
    }

}
