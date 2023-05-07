package com.dev.managementtaskapi.services.impl;

import com.dev.managementtaskapi.dtos.ChangeTaskIntoSubTaskDto;
import com.dev.managementtaskapi.dtos.CreateTaskDto;
import com.dev.managementtaskapi.exceptions.EntidadeNaoEncontradaException;
import com.dev.managementtaskapi.models.SubTaskModel;
import com.dev.managementtaskapi.models.TaskModel;
import com.dev.managementtaskapi.repositories.TaskRepository;
import com.dev.managementtaskapi.services.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    @Override
    public TaskModel createTask(TaskModel taskModel) {

        taskModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        taskModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        var taskInserted = taskRepository.save(taskModel);

        if (taskModel.getSubTasks() != null) {
            var subTasks = taskModel.getSubTasks();
            subTasks.forEach(x -> x.setTask(taskInserted));
            taskInserted.getSubTasks().addAll(subTasks);
        }

        return taskInserted;
    }

    @Override
    public List<TaskModel> findAll() {
        return taskRepository.findAllTasks();
    }

    @Override
    public TaskModel findById(UUID taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new EntidadeNaoEncontradaException("A Tarefa não foi encontrada"));
    }

    @Override
    public void deleteTask(TaskModel taskModel) {
        taskRepository.delete(taskModel);
    }

    @Override
    public void updateTask(UUID id, CreateTaskDto createTaskDto) {

        var task = findById(id);
        task.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        BeanUtils.copyProperties(createTaskDto, task, "id", "subTasks");

        taskRepository.save(task);

    }

    @Transactional
    @Override
    public void changeTaskIntoSubTask(UUID taskId, ChangeTaskIntoSubTaskDto changeTaskIntoSubTaskDto) {

        var taskCurrent = findById(taskId);
        var taskSubTask = findById(changeTaskIntoSubTaskDto.getTaskId());

        var subTask = new SubTaskModel();
        subTask.setTask(taskCurrent);
        subTask.setTitle(taskSubTask.getTitle());
        subTask.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        subTask.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        taskCurrent.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        taskCurrent.getSubTasks().add(subTask);

        taskRepository.delete(taskSubTask);

    }
}
