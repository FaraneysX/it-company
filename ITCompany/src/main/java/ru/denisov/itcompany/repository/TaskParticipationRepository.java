package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.TaskParticipation;
import ru.denisov.itcompany.exception.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskParticipationRepository implements BaseRepository<Long, TaskParticipation> {
    private static final String TABLE_NAME = "task_participation";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_NAME +
                    "(task_id, employee_id) " +
                    "VALUES(?, ?)";

    private static final String SELECT_ALL_TEMPLATE =
            "SELECT id, task_id, employee_id " +
                    "FROM " + TABLE_NAME;

    private static final String SELECT_BY_ID_TEMPLATE =
            "SELECT id, task_id, employee_id " +
                    "FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_NAME +
                    " SET task_id = ?, employee_id = ? " +
                    "WHERE id = ?";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final Logger LOGGER = Logger.getLogger(TaskParticipationRepository.class.getName());
    private Connection connection;

    @Override
    public void insert(TaskParticipation entity) throws RepositoryException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE)) {
            statement.setLong(1, entity.taskId());
            statement.setLong(2, entity.employeeId());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка добавления участия в задаче: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<TaskParticipation> findAll() throws RepositoryException {
        List<TaskParticipation> taskParticipation = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                taskParticipation.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска всех участий в задачах: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return taskParticipation;
    }

    @Override
    public Optional<TaskParticipation> findById(Long id) throws RepositoryException {
        Optional<TaskParticipation> taskParticipation = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                taskParticipation = Optional.of(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска участия в задаче по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return taskParticipation;
    }

    @Override
    public void update(Long id, TaskParticipation updatedEntity) throws RepositoryException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(3, updatedEntity.id());
            statement.setLong(1, updatedEntity.taskId());
            statement.setLong(2, updatedEntity.employeeId());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка изменения участия в задаче по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_TEMPLATE)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка удаления участия в задаче по ID: " + e.getMessage());

            throw new RepositoryException(e);
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