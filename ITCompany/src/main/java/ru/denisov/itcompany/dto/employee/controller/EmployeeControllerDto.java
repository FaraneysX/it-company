package ru.denisov.itcompany.dto.employee.controller;

import lombok.Builder;
import ru.denisov.itcompany.entity.Role;

import java.time.LocalDate;

@Builder
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
