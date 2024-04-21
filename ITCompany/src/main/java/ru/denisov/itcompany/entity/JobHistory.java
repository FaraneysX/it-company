package ru.denisov.itcompany.entity;

import java.time.LocalDate;

public record JobHistory(Long id,
                         Long employeeId,
                         Long jobId,
                         LocalDate startDate,
                         LocalDate endDate) {
}
