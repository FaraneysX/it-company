package ru.denisov.itcompany.service;

import ru.denisov.itcompany.dto.employee.controller.EmployeeControllerDto;
import ru.denisov.itcompany.dto.employee.controller.EmployeeLoginControllerDto;
import ru.denisov.itcompany.dto.employee.controller.EmployeePasswordControllerDto;
import ru.denisov.itcompany.dto.employee.controller.EmployeeRegistrationControllerDto;
import ru.denisov.itcompany.entity.Employee;
import ru.denisov.itcompany.mapper.EmployeeMapper;
import ru.denisov.itcompany.processing.HashPassword;
import ru.denisov.itcompany.processing.validator.login.LoginError;
import ru.denisov.itcompany.repository.EmployeeRepository;
import ru.denisov.itcompany.service.login.LoginResult;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class EmployeeService {
    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public EmployeeService(EmployeeRepository repository, EmployeeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void insert(EmployeeRegistrationControllerDto registrationControllerDto) {
        repository.insert(mapper.mapToEntity(registrationControllerDto));
    }

    public LoginResult login(EmployeeLoginControllerDto loginControllerDto) throws NoSuchAlgorithmException {
        EmployeePasswordControllerDto passwordControllerDto = repository.findPasswordByLogin(loginControllerDto.email());

        if (passwordControllerDto == null || passwordControllerDto.password().isEmpty() ||
                !HashPassword.verify(loginControllerDto.password(), passwordControllerDto.password().orElse(""))) {
            return new LoginResult(LoginError.USER_NOT_FOUND, Optional.empty());
        }

        Employee employee = repository.findByLogin(loginControllerDto.email());
        EmployeeControllerDto controllerDto = mapper.mapToController(employee);

        return new LoginResult(null, Optional.of(controllerDto));
    }

    public void update(EmployeeControllerDto controllerDto) {
        repository.update(mapper.mapToEntity(controllerDto));
    }
}
