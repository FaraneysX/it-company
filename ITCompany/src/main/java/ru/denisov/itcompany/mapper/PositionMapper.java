package ru.denisov.itcompany.mapper;

import ru.denisov.itcompany.dto.position.controller.PositionControllerDto;
import ru.denisov.itcompany.dto.position.view.PositionViewDto;
import ru.denisov.itcompany.entity.Position;

public class PositionMapper {
    public Position map(PositionControllerDto obj) {
        return Position.builder()
                .id(obj.id())
                .name(obj.name())
                .build();
    }

    public PositionControllerDto map(Position obj) {
        return PositionControllerDto.builder()
                .id(obj.id())
                .name(obj.name())
                .build();
    }

    public PositionViewDto mapController(PositionControllerDto obj) {
        return PositionViewDto.builder()
                .id(obj.id().toString())
                .name(obj.name())
                .build();
    }
}
