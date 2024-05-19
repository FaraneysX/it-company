package ru.denisov.itcompany.dto.employee.controller;

import lombok.Builder;

import java.util.Optional;

@Builder
public record EmployeePasswordControllerDto(Optional<String> password) {
}
