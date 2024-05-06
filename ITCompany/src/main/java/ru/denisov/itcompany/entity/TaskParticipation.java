package ru.denisov.itcompany.entity;

public record TaskParticipation(Long id,
                                Long taskId,
                                Long employeeId) {
}
