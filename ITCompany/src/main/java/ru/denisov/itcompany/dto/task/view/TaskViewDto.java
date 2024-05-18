package ru.denisov.itcompany.dto.task.view;

import lombok.Builder;

@Builder
public record TaskViewDto(String id,
                          String projectId,
                          String name,
                          String startDate,
                          String endDate) {
}
