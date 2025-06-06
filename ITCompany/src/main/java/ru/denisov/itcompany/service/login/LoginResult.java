package ru.denisov.itcompany.service.login;

import ru.denisov.itcompany.dto.employee.controller.EmployeeControllerDto;
import ru.denisov.itcompany.processing.validator.login.LoginError;

import java.util.Optional;

public record LoginResult(LoginError error, Optional<EmployeeControllerDto> controllerDto) {
}
