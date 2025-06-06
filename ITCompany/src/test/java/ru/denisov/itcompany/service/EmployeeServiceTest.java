package ru.denisov.itcompany.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
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
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {
    private static Employee employee;

    @Mock
    private EmployeeRepository repository;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private EmployeeService service;

    @BeforeAll
    public static void init() {
        employee = new Employee(
                null,
                null,
                "John",
                "Doe",
                LocalDate.of(1985, 12, 15),
                "john.doe@example.com",
                "password123"
        );
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insert() {
        EmployeeRegistrationControllerDto registrationDto = new EmployeeRegistrationControllerDto(
                "John",
                "Doe",
                LocalDate.of(1985, 12, 15),
                "john.doe@example.com",
                "password123"
        );

        when(mapper.mapToEntity(registrationDto)).thenReturn(employee);

        service.insert(registrationDto);

        verify(repository, times(1)).insert(employee);
    }

    @Test
    void loginSuccess() throws NoSuchAlgorithmException {
        String hashedPassword = HashPassword.hash(employee.getPassword());
        EmployeeLoginControllerDto loginDto = new EmployeeLoginControllerDto(employee.getEmail(), employee.getPassword());
        EmployeePasswordControllerDto passwordDto = new EmployeePasswordControllerDto(Optional.of(hashedPassword));

        EmployeeControllerDto controllerDto = new EmployeeControllerDto(
                null,
                null,
                "John",
                "Doe",
                LocalDate.of(1985, 12, 15),
                "john.doe@example.com",
                "password123"
        );

        when(repository.findPasswordByLogin(employee.getEmail())).thenReturn(passwordDto);

        try (MockedStatic<HashPassword> mockedHashPassword = mockStatic(HashPassword.class)) {
            mockedHashPassword.when(() -> HashPassword.verify(employee.getPassword(), hashedPassword)).thenReturn(true);

            when(repository.findByLogin(employee.getEmail())).thenReturn(employee);
            when(mapper.mapToController(employee)).thenReturn(controllerDto);

            LoginResult result = service.login(loginDto);

            assertNotNull(result);
            assertNull(result.error());
            assertTrue(result.controllerDto().isPresent());
            assertEquals(controllerDto, result.controllerDto().get());
        }
    }

    @Test
    void loginFail() throws NoSuchAlgorithmException {
        String hashedPassword = HashPassword.hash(employee.getPassword());
        EmployeeLoginControllerDto loginDto = new EmployeeLoginControllerDto(employee.getEmail(), employee.getPassword());
        EmployeePasswordControllerDto passwordDto = new EmployeePasswordControllerDto(Optional.of(hashedPassword));

        when(repository.findPasswordByLogin(employee.getEmail())).thenReturn(passwordDto);

        try (MockedStatic<HashPassword> mockedHashPassword = mockStatic(HashPassword.class)) {
            mockedHashPassword.when(() -> HashPassword.verify(employee.getPassword(), hashedPassword)).thenReturn(false);

            LoginResult result = service.login(loginDto);

            assertNotNull(result);
            assertEquals(LoginError.USER_NOT_FOUND, result.error());
            assertTrue(result.controllerDto().isEmpty());
        }
    }

    @Test
    void update() {
        EmployeeControllerDto controllerDto = new EmployeeControllerDto(
                1L,
                1L,
                "Johnn",
                "Doe",
                LocalDate.of(1985, 12, 15),
                "john.doe@example.com",
                "newpassword"
        );
        Employee updatedEmployee = new Employee(
                1L,
                1L,
                "John",
                "Doe",
                LocalDate.of(1985, 12, 15),
                "john.doe@example.com",
                "newpassword"
        );

        when(mapper.mapToEntity(controllerDto)).thenReturn(updatedEmployee);

        service.update(controllerDto);

        verify(repository, times(1)).update(updatedEmployee);
    }
}
