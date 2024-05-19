package ru.denisov.itcompany.mapper;

import ru.denisov.itcompany.dto.position.controller.PositionControllerDto;
import ru.denisov.itcompany.dto.position.view.PositionViewDto;
import ru.denisov.itcompany.entity.Position;

public class PositionMapper {
    public Position mapToEntity(PositionControllerDto obj) {
        return Position.builder()
                .id(obj.id())
                .name(obj.name())
                .build();
    }

    public PositionControllerDto mapToController(Position obj) {
        return PositionControllerDto.builder()
                .id(obj.getId())
                .name(obj.getName())
                .build();
    }

    public PositionViewDto mapToView(PositionControllerDto obj) {
        return PositionViewDto.builder()
                .id(obj.id().toString())
                .name(obj.name())
                .build();
    }
}
