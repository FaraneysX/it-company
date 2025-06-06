package ru.denisov.itcompany.service;

import ru.denisov.itcompany.dto.project.controller.ProjectControllerDto;
import ru.denisov.itcompany.mapper.ProjectMapper;
import ru.denisov.itcompany.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    public Long insertID(ProjectControllerDto controllerDto) {
        return repository.insertID(mapper.mapToEntity(controllerDto));
    }

    public ProjectControllerDto findById(Long id) {
        return mapper.mapToController(repository.findById(id));
    }

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    public ProjectControllerDto findByName(String name) {
        return mapper.mapToController(repository.findByName(name));
    }

    public List<ProjectControllerDto> findByEmployeeId(Long id) {
        return repository.findByEmployeeId(id).stream().map(mapper::mapToController).collect(Collectors.toList());
    }

    public void update(ProjectControllerDto controllerDto) {
        repository.update(mapper.mapToEntity(controllerDto));
    }

    public void delete(Long id) {
        repository.delete(id);
    }

    public void delete(String name) {
        repository.delete(name);
    }
}
