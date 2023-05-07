package com.dev.managementtaskapi.services.impl;

import com.dev.managementtaskapi.dtos.CreateSubTaskDto;
import com.dev.managementtaskapi.exceptions.EntidadeNaoEncontradaException;
import com.dev.managementtaskapi.models.SubTaskModel;
import com.dev.managementtaskapi.models.TaskModel;
import com.dev.managementtaskapi.repositories.SubTaskRepository;
import com.dev.managementtaskapi.services.SubTaskService;
import com.dev.managementtaskapi.services.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class SubTaskServiceImpl implements SubTaskService {

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private TaskService taskService;

    @Transactional
    @Override
    public SubTaskModel createSubTask(UUID taskId, CreateSubTaskDto createSubTaskDto) {

        var taskModel = taskService.findById(taskId);

        var subTaskModel = new SubTaskModel();
        BeanUtils.copyProperties(createSubTaskDto, subTaskModel);
        subTaskModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        subTaskModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        subTaskModel.setTask(taskModel);

        taskModel.getSubTasks().add(subTaskModel);

        return subTaskModel;

    }

    @Override
    public void updateSubTask(UUID taskId, UUID subTaskId, CreateSubTaskDto createSubTaskDto) {

        var subTaskModel = subTaskRepository.findSubTaskModelByTaskAndSubTaskId(taskId, subTaskId);

        if (subTaskModel.isPresent()) {

            BeanUtils.copyProperties(createSubTaskDto, subTaskModel.get(), "id", "task");
            subTaskModel.get().setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            subTaskRepository.save(subTaskModel.get());
        } else {
            throw new EntidadeNaoEncontradaException("A Sub Tarefa não foi encontrada");
        }

    }

    @Transactional
    @Override
    public void deleteSubTask(UUID taskId, UUID subTaskId) {
        subTaskRepository.deleteSubTaskByTaskIdAndSubTaskId(taskId, subTaskId);
    }

    @Transactional
    @Override
    public void changeSubTaskIntoTask(UUID subTaskId) {

        var subTaskModel = findById(subTaskId);

        subTaskRepository.delete(subTaskModel);

        var taskModel = new TaskModel();
        taskModel.setTitle(subTaskModel.getTitle());
        taskService.createTask(taskModel);

    }

    @Transactional
    @Override
    public void changeTaskFromSubTask(UUID subTaskId, UUID taskId) {

        var subTaskModel = findById(subTaskId);
        var taskModel = taskService.findById(taskId);
        taskModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        subTaskModel.setTask(taskModel);
        taskModel.getSubTasks().add(subTaskModel);

    }

    @Override
    public SubTaskModel findById(UUID subTaskId) {
        return subTaskRepository.findById(subTaskId).orElseThrow(() -> new EntidadeNaoEncontradaException("A Sub Tarefa não foi encontrada"));
    }

}
