package ru.denisov.itcompany.dto.employee.view;

import lombok.Builder;

@Builder
public record EmployeeLoginViewDto(String email,
                                   String password) {
}
