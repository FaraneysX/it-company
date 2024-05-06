package ru.denisov.itcompany.entity;

import java.time.LocalDate;

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
