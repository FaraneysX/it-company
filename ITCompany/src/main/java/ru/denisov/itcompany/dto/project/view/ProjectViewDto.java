package ru.denisov.itcompany.dto.project.view;

import lombok.Builder;

@Builder
public record ProjectViewDto(String id,
                             String name,
                             String startDate) {
}
