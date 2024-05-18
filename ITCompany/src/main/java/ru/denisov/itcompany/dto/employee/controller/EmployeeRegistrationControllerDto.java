package ru.denisov.itcompany.dto.employee.controller;

import java.time.LocalDate;

public record EmployeeRegistrationControllerDto(String name,
                                                String surname,
                                                LocalDate birthDate,
                                                String email,
                                                String password) {
}
