package ru.denisov.itcompany.entity;

import java.time.LocalDate;

public record Employee(Long id,
                       String name,
                       String surname,
                       String patronymic,
                       LocalDate birthDate,
                       String email,
                       String password,
                       Role role) {
}
