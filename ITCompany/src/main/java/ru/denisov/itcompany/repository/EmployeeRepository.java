package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.Employee;
import ru.denisov.itcompany.entity.Role;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository implements BaseRepository<Long, Employee> {
    private static final String TABLE_NAME = "employee";
    private static final String INSERT_TEMPLATE = "INSERT INTO " + TABLE_NAME +
            "(project_id, position_id, name, surname, birth_date, email, password, role)" +
            " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_TEMPLATE = "SELECT * FROM " + TABLE_NAME;
    private static final String SELECT_BY_ID_TEMPLATE = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String UPDATE_TEMPLATE = "UPDATE " + TABLE_NAME +
            " SET project_id = ?, position_id = ?, email = ?, password = ?, role = ? WHERE id = ?";
    private static final String DELETE_TEMPLATE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

    private Connection connection;

    @Override
    public void insert(Employee entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE)) {
            statement.setLong(1, entity.projectId());
            statement.setLong(2, entity.positionId());
            statement.setString(3, entity.name());
            statement.setString(4, entity.surname());
            statement.setDate(5, Date.valueOf(entity.birthDate()));
            statement.setString(6, entity.email());
            statement.setString(7, entity.password());
            statement.setString(8, entity.role().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new LinkedList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                employees.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employees;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        Optional<Employee> employee = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                employee = Optional.of(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employee;
    }

    @Override
    public void update(Long id, Employee updatedEntity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(6, id);
            statement.setLong(1, updatedEntity.projectId());
            statement.setLong(2, updatedEntity.positionId());
            statement.setString(3, updatedEntity.email());
            statement.setString(4, updatedEntity.password());
            statement.setString(5, updatedEntity.role().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_TEMPLATE)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Employee mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Employee(
                resultSet.getLong("id"),
                resultSet.getLong("project_id"),
                resultSet.getLong("position_id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getDate("birth_date").toLocalDate(),
                resultSet.getString("email"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }
}
