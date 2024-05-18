package ru.denisov.itcompany.entity;

import lombok.Builder;

@Builder
public record TaskParticipation(Long id,
                                Long taskId,
                                Long employeeId) {
}
