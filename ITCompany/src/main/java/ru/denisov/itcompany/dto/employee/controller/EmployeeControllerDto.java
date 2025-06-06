package ru.denisov.itcompany.dto.employee.controller;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmployeeControllerDto(Long id,
                                    Long projectId,
                                    String name,
                                    String surname,
                                    LocalDate birthDate,
                                    String email,
                                    String password) {
}
