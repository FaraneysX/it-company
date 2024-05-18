package ru.denisov.itcompany.dto.taskparticipation.controller;

import lombok.Builder;

@Builder
public record TaskParticipationControllerDto(Long id,
                                             Long taskId,
                                             Long employeeId) {
}
