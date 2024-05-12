package ru.denisov.itcompany.dto.project.controller;

import java.time.LocalDate;

public record ProjectControllerDto(Long id,
                                   String name,
                                   LocalDate startDate) {
}
