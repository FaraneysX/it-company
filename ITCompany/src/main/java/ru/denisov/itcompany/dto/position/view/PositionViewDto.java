package ru.denisov.itcompany.dto.position.view;

import lombok.Builder;

@Builder
public record PositionViewDto(String id,
                              String name) {
}
