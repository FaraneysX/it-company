package ru.denisov.itcompany.dto.employee.view;

public record EmployeeRegistrationViewDto(String name,
                                          String surname,
                                          String birthDate,
                                          String email,
                                          String password) {
}
