package ru.denisov.itcompany.service;

import ru.denisov.itcompany.dto.project.controller.ProjectControllerDto;
import ru.denisov.itcompany.mapper.ProjectMapper;
import ru.denisov.itcompany.repository.ProjectRepository;

public class ProjectService {
    private final ProjectRepository repository;
    private final ProjectMapper mapper;

    public ProjectService(ProjectRepository repository, ProjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void insert(ProjectControllerDto controllerDto) {
        repository.insert(mapper.mapToEntity(controllerDto));
    }

    public ProjectControllerDto findById(Long id) {
        return mapper.mapToController(repository.findById(id));
    }

    public void update(ProjectControllerDto controllerDto) {
        repository.update(mapper.mapToEntity(controllerDto));
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
