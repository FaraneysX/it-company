package ru.denisov.itcompany.dto.taskparticipation.view;

import lombok.Builder;

@Builder
public record TaskParticipationViewDto(String id,
                                       String taskId,
                                       String employeeId) {
}
