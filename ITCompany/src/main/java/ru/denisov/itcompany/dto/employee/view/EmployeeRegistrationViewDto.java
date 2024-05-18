package ru.denisov.itcompany.dto.employee.view;

import lombok.Builder;

@Builder
public record EmployeeRegistrationViewDto(String name,
                                          String surname,
                                          String birthDate,
                                          String email,
                                          String password) {
}
