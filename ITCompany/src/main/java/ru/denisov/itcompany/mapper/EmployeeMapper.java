package ru.denisov.itcompany.mapper;

import ru.denisov.itcompany.dto.employee.controller.EmployeeControllerDto;
import ru.denisov.itcompany.dto.employee.controller.EmployeeLoginControllerDto;
import ru.denisov.itcompany.dto.employee.controller.EmployeeRegistrationControllerDto;
import ru.denisov.itcompany.dto.employee.view.EmployeeLoginViewDto;
import ru.denisov.itcompany.dto.employee.view.EmployeeRegistrationViewDto;
import ru.denisov.itcompany.dto.employee.view.EmployeeViewDto;
import ru.denisov.itcompany.entity.Employee;
import ru.denisov.itcompany.processing.HashPassword;

import java.time.LocalDate;

public class EmployeeMapper {
    public Employee mapToEntity(EmployeeControllerDto obj) {
        return Employee.builder()
                .id(obj.id())
                .projectId(obj.projectId())
                .name(obj.name())
                .surname(obj.surname())
                .birthDate(obj.birthDate())
                .email(obj.email())
                .password(obj.password())
                .build();
    }

    public Employee mapToEntity(EmployeeRegistrationControllerDto obj) {
        return Employee.builder()
                .name(obj.name())
                .surname(obj.surname())
                .birthDate(obj.birthDate())
                .email(obj.email())
                .password(HashPassword.hash(obj.password()))
                .build();
    }

    public EmployeeControllerDto mapToController(Employee obj) {
        return EmployeeControllerDto.builder()
                .id(obj.getId())
                .projectId(obj.getProjectId())
                .name(obj.getName())
                .surname(obj.getSurname())
                .birthDate(obj.getBirthDate())
                .email(obj.getEmail())
                .password(obj.getPassword())
                .build();
    }

    public EmployeeLoginControllerDto mapToLoginController(EmployeeLoginViewDto obj) {
        return EmployeeLoginControllerDto.builder()
                .email(obj.email())
                .password(obj.password())
                .build();
    }

    public EmployeeViewDto mapToView(EmployeeControllerDto obj) {
        return EmployeeViewDto.builder()
                .id(obj.id().toString())
                .projectId(obj.projectId().toString())
                .name(obj.name())
                .surname(obj.surname())
                .birthDate(obj.birthDate().toString())
                .email(obj.email())
                .password(obj.password())
                .build();
    }

    public EmployeeRegistrationControllerDto mapToRegistrationController(EmployeeRegistrationViewDto obj) {
        return EmployeeRegistrationControllerDto.builder()
                .name(obj.name())
                .surname(obj.surname())
                .birthDate(LocalDate.parse(obj.birthDate()))
                .email(obj.email())
                .password(obj.password())
                .build();
    }
}
