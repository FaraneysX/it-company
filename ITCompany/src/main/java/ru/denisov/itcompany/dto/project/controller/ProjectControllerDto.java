package ru.denisov.itcompany.dto.project.controller;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProjectControllerDto(Long id,
                                   String name,
                                   LocalDate startDate) {
}
