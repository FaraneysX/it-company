package ru.denisov.itcompany.dto.task.controller;

import java.time.LocalDate;

public record TaskControllerDto(Long id,
                                Long projectId,
                                String name,
                                LocalDate startDate,
                                LocalDate endDate) {
}
