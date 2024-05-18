package ru.denisov.itcompany.entity;

import lombok.Builder;

@Builder
public record Position(Long id,
                       String name) {
}
