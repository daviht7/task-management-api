package com.dev.managementtaskapi.services;

import com.dev.managementtaskapi.dtos.CreateSubTaskDto;
import com.dev.managementtaskapi.models.SubTaskModel;

import java.util.UUID;

public interface SubTaskService {
    SubTaskModel createSubTask(UUID taskId, CreateSubTaskDto createSubTaskDto);

    void updateSubTask(UUID taskId, UUID subTaskId, CreateSubTaskDto createSubTaskDto);

    void deleteSubTask(UUID taskId, UUID subTaskId);

    void changeSubTaskIntoTask(UUID subTaskId);

    void changeTaskFromSubTask(UUID subTaskId, UUID taskId);

    SubTaskModel findById(UUID subTaskId);
}
