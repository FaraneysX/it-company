package ru.denisov.itcompany.dto.task.view;

public record TaskViewDto(String id,
                          String projectId,
                          String name,
                          String startDate,
                          String endDate) {
}
