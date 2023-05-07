package com.dev.managementtaskapi.services;

import com.dev.managementtaskapi.dtos.ChangeTaskIntoSubTaskDto;
import com.dev.managementtaskapi.dtos.CreateTaskDto;
import com.dev.managementtaskapi.models.TaskModel;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskModel createTask(TaskModel taskModel);

    List<TaskModel> findAll();

    TaskModel findById(UUID taskId);

    void deleteTask(TaskModel taskModel);

    void updateTask(UUID id, CreateTaskDto createTaskDto);

    void changeTaskIntoSubTask(UUID taskId, ChangeTaskIntoSubTaskDto changeTaskIntoSubTaskDto);
}
