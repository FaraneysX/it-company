package ru.denisov.itcompany.dto.task.controller;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TaskControllerDto(Long id,
                                Long projectId,
                                String name,
                                LocalDate startDate,
                                LocalDate endDate) {
}
