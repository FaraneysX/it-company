package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.dto.employee.controller.EmployeePasswordControllerDto;
import ru.denisov.itcompany.entity.Employee;
import ru.denisov.itcompany.exception.RepositoryException;
import ru.denisov.itcompany.processing.ConnectionGetter;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeRepository implements BaseRepository<Long, Employee> {
    private static final String TABLE_NAME = "employee";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_NAME +
                    "(name, surname, birth_date, email, password) " +
                    "VALUES(?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_TEMPLATE =
            "SELECT id, project_id, name, surname, birth_date, email, password " +
                    "FROM " + TABLE_NAME;

    private static final String SELECT_BY_ID_TEMPLATE =
            "SELECT id, project_id, name, surname, birth_date, email, password " +
                    "FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final String CLEAR_PROJECT_ID_TEMPLATE =
            "UPDATE " + TABLE_NAME +
                    " SET project_id = NULL " +
                    " WHERE project_id = ?";

    private static final String SELECT_BY_LOGIN_TEMPLATE =
            "SELECT id, project_id, name, surname, birth_date, password " +
                    "FROM " + TABLE_NAME +
                    " WHERE email = ?";

    private static final String SELECT_PASSWORD =
            "SELECT password " +
                    "FROM " + TABLE_NAME +
                    " WHERE email = ?";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_NAME +
                    " SET project_id = ?, email = ?, password = ? " +
                    "WHERE id = ?";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final String SELECT_EMPLOYEE_BY_EMAIL =
            "SELECT 1 " +
                    "FROM employee " +
                    "WHERE email = ?";

    private static final Logger LOGGER = Logger.getLogger(EmployeeRepository.class.getName());
    private final ConnectionGetter connectionGetter;

    public EmployeeRepository(ConnectionGetter connectionGetter) {
        this.connectionGetter = connectionGetter;
    }

    @Override
    public void insert(Employee entity) throws RepositoryException {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setDate(3, Date.valueOf(entity.getBirthDate()));
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getPassword());

            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();

            if (keys.next()) {
                entity.setId(keys.getLong("id"));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка добавления сотрудника: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Employee> findAll() throws RepositoryException {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                employees.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска всех сотрудников: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return employees;
    }

    @Override
    public Employee findById(Long id) throws RepositoryException {
        Employee employee = null;

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                employee = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска сотрудника по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return employee;
    }

    public EmployeePasswordControllerDto findPasswordByLogin(String login) throws RepositoryException {
        String password = null;

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_PASSWORD)) {
            statement.setString(1, login);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    password = resultSet.getString("password");
                }
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска пароля сотрудника по логину: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return EmployeePasswordControllerDto.builder()
                .password(Optional.ofNullable(password))
                .build();
    }

    public Employee findByLogin(String login) throws RepositoryException {
        Employee employee = null;

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_LOGIN_TEMPLATE)) {
            statement.setString(1, login);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                employee = new Employee(
                        resultSet.getLong("id"),
                        resultSet.getLong("project_id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getDate("birth_date").toLocalDate(),
                        login,
                        resultSet.getString("password")
                );
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска пароля сотрудника по логину: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return employee;
    }

    @Override
    public void update(Employee updatedEntity) throws RepositoryException {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(4, updatedEntity.getId());
            statement.setLong(1, updatedEntity.getProjectId());
            statement.setString(2, updatedEntity.getEmail());
            statement.setString(3, updatedEntity.getPassword());

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка изменения сотрудника по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    public void deleteProjectId(Long projectId) throws RepositoryException {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(CLEAR_PROJECT_ID_TEMPLATE)) {
            statement.setLong(1, projectId);

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка изменения сотрудника по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(DELETE_TEMPLATE)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка удаления сотрудника по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    public boolean existsByEmail(String email) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_EMPLOYEE_BY_EMAIL)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException | InterruptedException e) {
            throw new RepositoryException(e);
        }
    }

    private Employee mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Employee(
                resultSet.getLong("id"),
                resultSet.getLong("project_id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getDate("birth_date").toLocalDate(),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
