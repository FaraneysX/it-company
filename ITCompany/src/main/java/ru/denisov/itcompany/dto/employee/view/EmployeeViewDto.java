package ru.denisov.itcompany.dto.employee.view;

import lombok.Builder;

@Builder
public record EmployeeViewDto(String id,
                              String projectId,
                              String name,
                              String surname,
                              String birthDate,
                              String email,
                              String password) {
}
