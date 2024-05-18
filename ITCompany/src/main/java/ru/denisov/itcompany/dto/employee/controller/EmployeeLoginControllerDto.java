package ru.denisov.itcompany.dto.employee.controller;

import lombok.Builder;

@Builder
public record EmployeeLoginControllerDto(String email,
                                         String password) {
}
