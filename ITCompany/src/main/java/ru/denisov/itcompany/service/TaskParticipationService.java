package ru.denisov.itcompany.service;

import ru.denisov.itcompany.dto.taskparticipation.controller.TaskParticipationControllerDto;
import ru.denisov.itcompany.mapper.TaskParticipationMapper;
import ru.denisov.itcompany.repository.TaskParticipationRepository;

public class TaskParticipationService {
    private final TaskParticipationRepository repository;
    private final TaskParticipationMapper mapper;

    public TaskParticipationService(TaskParticipationRepository repository, TaskParticipationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void insert(TaskParticipationControllerDto controllerDto) {
        repository.insert(mapper.mapToEntity(controllerDto));
    }

    public TaskParticipationControllerDto findById(Long id) {
        return mapper.mapToController(repository.findById(id));
    }

    public void update(TaskParticipationControllerDto controllerDto) {
        repository.update(mapper.mapToEntity(controllerDto));
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
