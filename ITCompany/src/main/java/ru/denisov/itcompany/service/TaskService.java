package ru.denisov.itcompany.service;

import ru.denisov.itcompany.dto.task.controller.TaskControllerDto;
import ru.denisov.itcompany.mapper.TaskMapper;
import ru.denisov.itcompany.repository.TaskRepository;

public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public void insert(TaskControllerDto controllerDto) {
        taskRepository.insert(taskMapper.mapToEntity(controllerDto));
    }

    public TaskControllerDto findById(Long id) {
        return taskMapper.mapToController(taskRepository.findById(id));
    }

    public void update(TaskControllerDto controllerDto) {
        taskRepository.update(taskMapper.mapToEntity(controllerDto));
    }

    public void delete(Long id) {
        taskRepository.delete(id);
    }
}
