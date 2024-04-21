package ru.denisov.itcompany.entity;

import java.time.LocalDate;

public record Task(Long id,
                   Long projectId,
                   String name,
                   LocalDate startDate,
                   LocalDate endDate) {
}