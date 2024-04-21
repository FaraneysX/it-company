package ru.denisov.itcompany.entity;

public record TaskParticipation(Long id,
                                Long employeeId,
                                Long taskId) {
}
