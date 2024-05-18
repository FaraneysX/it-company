package ru.denisov.itcompany.entity;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Task(Long id,
                   Long projectId,
                   String name,
                   LocalDate startDate,
                   LocalDate endDate) {
}
