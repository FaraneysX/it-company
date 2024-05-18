package ru.denisov.itcompany.dto.position.controller;

import lombok.Builder;

@Builder
public record PositionControllerDto(Long id,
                                    String name) {
}
