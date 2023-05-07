package com.dev.managementtaskapi;

import com.dev.managementtaskapi.dtos.CreateTaskDto;
import com.dev.managementtaskapi.models.TaskModel;
import com.dev.managementtaskapi.repositories.TaskRepository;
import com.dev.managementtaskapi.services.TaskService;
import com.dev.managementtaskapi.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class TaskServiceTest {

    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    public TaskServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateTask() {

        var createTaskDto = new CreateTaskDto();
        createTaskDto.setTitle("fazer caminhada");

        TaskModel taskModel = new TaskModel();
        BeanUtils.copyProperties(createTaskDto, taskModel);

        var taskRepository = Mockito.mock(TaskRepository.class);
        Mockito.when(taskRepository.save(taskModel)).thenReturn(taskModel);

        taskService = new TaskServiceImpl(taskRepository);

        var taskCreated = taskService.createTask(taskModel);

        Mockito.verify(taskRepository).save(taskModel);
        Assertions.assertEquals(taskCreated.getTitle(), createTaskDto.getTitle());

    }

    @Test
    public void shouldFindAllTasks() {

        var tasks = new ArrayList<TaskModel>();
        var taskModel = new TaskModel();
        taskModel.setTitle("fazer caminhada");
        tasks.add(taskModel);

        var taskRepository = Mockito.mock(TaskRepository.class);
        Mockito.when(taskRepository.findAllTasks()).thenReturn(tasks);

        taskService = new TaskServiceImpl(taskRepository);

        var tasksFinded = taskService.findAll();

        Mockito.verify(taskRepository).findAllTasks();
        Assertions.assertEquals(tasksFinded.stream().count(), tasks.stream().count());

    }

    @Test
    public void shouldFindById() {

        var uuid = UUID.randomUUID();

        var taskModel = new TaskModel();
        taskModel.setId(uuid);
        taskModel.setTitle("fazer caminhada");

        var taskModelOptional = Optional.of(taskModel);

        var taskRepository = Mockito.mock(TaskRepository.class);
        Mockito.when(taskRepository.findById(uuid)).thenReturn(taskModelOptional);

        taskService = new TaskServiceImpl(taskRepository);

        var taskFinded = taskService.findById(uuid);

        Mockito.verify(taskRepository).findById(uuid);
        Assertions.assertEquals(taskFinded.getId(), taskModel.getId());

    }

}
