package ru.denisov.itcompany.service;

import ru.denisov.itcompany.dto.task.controller.TaskControllerDto;
import ru.denisov.itcompany.mapper.TaskMapper;
import ru.denisov.itcompany.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void insert(TaskControllerDto controllerDto) {
        repository.insert(mapper.mapToEntity(controllerDto));
    }

    public Long insertID(TaskControllerDto controllerDto) {
        return repository.insertID(mapper.mapToEntity(controllerDto));
    }

    public TaskControllerDto findById(Long id) {
        return mapper.mapToController(repository.findById(id));
    }

    public List<TaskControllerDto> findByEmployeeId(Long id) {
        return repository.findByEmployeeId(id).stream()
                .map(mapper::mapToController).collect(Collectors.toList());
    }

    public void update(TaskControllerDto controllerDto) {
        repository.update(mapper.mapToEntity(controllerDto));
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public void deleteByProjectId(Long projectId) {
        repository.deleteByProjectId(projectId);
    }
}
