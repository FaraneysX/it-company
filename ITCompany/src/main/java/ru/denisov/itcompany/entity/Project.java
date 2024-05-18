package ru.denisov.itcompany.entity;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Project(Long id,
                      String name,
                      LocalDate startDate) {
}
