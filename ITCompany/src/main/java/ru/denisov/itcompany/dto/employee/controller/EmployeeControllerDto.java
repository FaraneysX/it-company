package ru.denisov.itcompany.dto.employee.controller;

import ru.denisov.itcompany.entity.Role;

import java.time.LocalDate;

public record EmployeeControllerDto(Long id,
                                    Long projectId,
                                    Long positionId,
                                    String name,
                                    String surname,
                                    LocalDate birthDate,
                                    String email,
                                    String password,
                                    Role role) {
}
