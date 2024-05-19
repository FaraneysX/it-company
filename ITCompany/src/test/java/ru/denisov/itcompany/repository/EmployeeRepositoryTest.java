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
import java.sql.Statement;
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
    private EmployeeRepository repository;

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
        repository = new EmployeeRepository(connectionGetter);
    }

    @Test
    void insert() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);

        ResultSet generatedKeys = mock(ResultSet.class);

        when(statement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong("id")).thenReturn(1L);

        repository.insert(employee);

        verify(statement).setLong(eq(1), eq(1L));
        verify(statement).setLong(eq(2), eq(1L));
        verify(statement).setString(eq(3), eq("Иван"));
        verify(statement).setString(eq(4), eq("Иванов"));

        verify(statement).executeUpdate();

        assertEquals(1L, employee.getId());
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

        List<Employee> foundEmployees = repository.findAll();

        verify(connection).prepareStatement(anyString());
        verify(statement).executeQuery();

        assertEquals(employees, foundEmployees);
    }

    @Test
    void findById() throws InterruptedException, SQLException {
        Long employeeId = 1L;

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(employee.getId());
        when(resultSet.getLong("project_id")).thenReturn(employee.getProjectId());
        when(resultSet.getLong("position_id")).thenReturn(employee.getPositionId());
        when(resultSet.getString("name")).thenReturn(employee.getName());
        when(resultSet.getString("surname")).thenReturn(employee.getSurname());
        when(resultSet.getDate("birth_date")).thenReturn(Date.valueOf(employee.getBirthDate()));
        when(resultSet.getString("email")).thenReturn(employee.getEmail());
        when(resultSet.getString("password")).thenReturn(employee.getPassword());
        when(resultSet.getString("role")).thenReturn(employee.getRole().toString());

        Employee foundEmployee = repository.findById(employeeId);

        verify(connection).prepareStatement(anyString());
        verify(statement).setLong(eq(1), eq(1L));

        verify(statement).executeQuery();

        assertEquals(employee, foundEmployee);
    }

    @Test
    void update() throws InterruptedException, SQLException {
        Employee updatedEmployee = new Employee(
                1L,
                2L,
                2L,
                "Иван",
                "Иванов",
                LocalDate.of(1990, 1, 1),
                "ivanov@example.com",
                "password",
                Role.USER
        );

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.update(updatedEmployee);

        verify(connection).prepareStatement(anyString());
        verify(statement).setLong(eq(6), eq(updatedEmployee.getId()));
        verify(statement).setLong(eq(1), eq(updatedEmployee.getProjectId()));
        verify(statement).setLong(eq(2), eq(updatedEmployee.getPositionId()));
        verify(statement).setString(eq(3), eq(updatedEmployee.getEmail()));
        verify(statement).setString(eq(4), eq(updatedEmployee.getPassword()));
        verify(statement).setString(eq(5), eq(updatedEmployee.getRole().toString()));

        verify(statement).executeUpdate();
    }

    @Test
    void delete() throws SQLException, InterruptedException {
        Long employeeId = 1L;

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.delete(employeeId);

        verify(connection).prepareStatement(anyString());
        verify(statement).setLong(eq(1), eq(employeeId));

        verify(statement).executeUpdate();
    }
}
