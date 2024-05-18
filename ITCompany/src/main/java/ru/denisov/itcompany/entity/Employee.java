package ru.denisov.itcompany.entity;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Employee(Long id,
                       Long projectId,
                       Long positionId,
                       String name,
                       String surname,
                       LocalDate birthDate,
                       String email,
                       String password,
                       Role role) {
}
