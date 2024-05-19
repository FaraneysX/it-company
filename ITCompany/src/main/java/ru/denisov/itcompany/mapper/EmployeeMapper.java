package ru.denisov.itcompany.mapper;

import ru.denisov.itcompany.dto.employee.controller.EmployeeControllerDto;
import ru.denisov.itcompany.dto.employee.controller.EmployeeLoginControllerDto;
import ru.denisov.itcompany.dto.employee.controller.EmployeeRegistrationControllerDto;
import ru.denisov.itcompany.dto.employee.view.EmployeeLoginViewDto;
import ru.denisov.itcompany.dto.employee.view.EmployeeViewDto;
import ru.denisov.itcompany.entity.Employee;
import ru.denisov.itcompany.processing.HashPassword;

public class EmployeeMapper {
    public Employee mapToEntity(EmployeeControllerDto obj) {
        return Employee.builder()
                .id(obj.id())
                .projectId(obj.projectId())
                .positionId(obj.positionId())
                .name(obj.name())
                .surname(obj.surname())
                .birthDate(obj.birthDate())
                .email(obj.email())
                .password(obj.password())
                .role(obj.role())
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
                .positionId(obj.getPositionId())
                .name(obj.getName())
                .surname(obj.getSurname())
                .birthDate(obj.getBirthDate())
                .email(obj.getEmail())
                .password(obj.getPassword())
                .role(obj.getRole())
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
                .positionId(obj.positionId().toString())
                .name(obj.name())
                .surname(obj.surname())
                .birthDate(obj.birthDate().toString())
                .email(obj.email())
                .password(obj.password())
                .role(obj.role().toString())
                .build();
    }
}
