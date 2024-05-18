package ru.denisov.itcompany.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.denisov.itcompany.entity.Employee;
import ru.denisov.itcompany.entity.Role;
import ru.denisov.itcompany.exception.RepositoryException;
import ru.denisov.itcompany.processing.ConnectionGetter;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeRepositoryTest {
    private static Employee employee;
    private static List<Employee> employees;
    private ConnectionGetter connectionGetter;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private EmployeeRepository employeeRepository;

    @BeforeAll
    static void init() {
        employee = new Employee(
                1L,
                1L,
                1L,
                "Иван",
                "Иванов",
                LocalDate.of(1990, 1, 1),
                "ivanov@example.com",
                "password",
                Role.USER
        );

        employees = new ArrayList<>();

        employees.add(new Employee(
                1L,
                1L,
                1L,
                "Иван",
                "Иванов",
                LocalDate.of(1990, 1, 1),
                "ivanov@example.com",
                "password1",
                Role.USER)
        );

        employees.add(new Employee(
                2L,
                2L,
                2L,
                "Петр",
                "Петров",
                LocalDate.of(1992, 3, 15),
                "petrov@example.com",
                "password2",
                Role.ADMIN)
        );
    }

    @BeforeEach
    void setUp() {
        connectionGetter = mock(ConnectionGetter.class);
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        employeeRepository = new EmployeeRepository(connectionGetter);
    }

    @Test
    void insert() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        employeeRepository.insert(employee);

        verify(connection).prepareStatement(anyString());

        verify(statement).setLong(eq(1), eq(1L));
        verify(statement).setLong(eq(2), eq(1L));
        verify(statement).setString(eq(3), eq("Иван"));
        verify(statement).setString(eq(4), eq("Иванов"));

        verify(statement).executeUpdate();
    }

    @Test
    void findAll() throws SQLException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(resultSet.getLong("id"))
                .thenReturn(1L)
                .thenReturn(2L);

        when(resultSet.getLong("project_id"))
                .thenReturn(1L)
                .thenReturn(2L);

        when(resultSet.getLong("position_id"))
                .thenReturn(1L)
                .thenReturn(2L);

        when(resultSet.getString("name"))
                .thenReturn("Иван")
                .thenReturn("Петр");

        when(resultSet.getString("surname"))
                .thenReturn("Иванов")
                .thenReturn("Петров");

        when(resultSet.getDate("birth_date"))
                .thenReturn(Date.valueOf(LocalDate.of(1990, 1, 1)))
                .thenReturn(Date.valueOf(LocalDate.of(1992, 3, 15)));

        when(resultSet.getString("email"))
                .thenReturn("ivanov@example.com")
                .thenReturn("petrov@example.com");

        when(resultSet.getString("password"))
                .thenReturn("password1")
                .thenReturn("password2");

        when(resultSet.getString("role"))
                .thenReturn("USER")
                .thenReturn("ADMIN");

        List<Employee> foundEmployees = employeeRepository.findAll();

        verify(connection).prepareStatement(anyString());

        verify(statement).executeQuery();

        assertEquals(employees, foundEmployees);
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}