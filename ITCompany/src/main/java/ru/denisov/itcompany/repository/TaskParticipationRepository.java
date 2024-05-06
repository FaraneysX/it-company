package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.TaskParticipation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TaskParticipationRepository implements BaseRepository<Long, TaskParticipation> {
    private static final String TABLE_NAME = "task_participation";
    private static final String INSERT_TEMPLATE = "INSERT INTO " + TABLE_NAME + "(task_id, employee_id) VALUES(?, ?)";
    private static final String SELECT_ALL_TEMPLATE = "SELECT * FROM " + TABLE_NAME;
    private static final String SELECT_BY_ID_TEMPLATE = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String UPDATE_TEMPLATE = "UPDATE " + TABLE_NAME + " SET task_id = ?, employee_id = ? WHERE id = ?";
    private static final String DELETE_TEMPLATE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

    private Connection connection;

    @Override
    public void insert(TaskParticipation entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE)) {
            statement.setLong(1, entity.taskId());
            statement.setLong(2, entity.employeeId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TaskParticipation> findAll() {
        List<TaskParticipation> taskParticipation = new LinkedList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                taskParticipation.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return taskParticipation;
    }

    @Override
    public Optional<TaskParticipation> findById(Long id) {
        Optional<TaskParticipation> taskParticipation = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                taskParticipation = Optional.of(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return taskParticipation;
    }

    @Override
    public void update(Long id, TaskParticipation updatedEntity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(3, updatedEntity.id());
            statement.setLong(1, updatedEntity.taskId());
            statement.setLong(2, updatedEntity.employeeId());

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

    private TaskParticipation mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new TaskParticipation(
                resultSet.getLong("id"),
                resultSet.getLong("task_id"),
                resultSet.getLong("employee_id")
        );
    }
}
