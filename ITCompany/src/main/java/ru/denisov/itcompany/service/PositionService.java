package ru.denisov.itcompany.service;

import ru.denisov.itcompany.dto.position.controller.PositionControllerDto;
import ru.denisov.itcompany.mapper.PositionMapper;
import ru.denisov.itcompany.repository.PositionRepository;

public class PositionService {
    private final PositionRepository repository;
    private final PositionMapper mapper;

    public PositionService(PositionRepository repository, PositionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void insert(PositionControllerDto controllerDto) {
        repository.insert(mapper.mapToEntity(controllerDto));
    }

    public PositionControllerDto findById(Long id) {
        return mapper.mapToController(repository.findById(id));
    }

    public void update(PositionControllerDto controllerDto) {
        repository.update(mapper.mapToEntity(controllerDto));
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
