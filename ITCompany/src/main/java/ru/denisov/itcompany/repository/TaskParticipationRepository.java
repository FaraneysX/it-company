package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.TaskParticipation;
import ru.denisov.itcompany.exception.RepositoryException;
import ru.denisov.itcompany.processing.ConnectionGetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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

    private static final String SELECT_BY_EMPLOYEE_ID_TEMPLATE =
            "SELECT id, task_id, employee_id " +
                    "FROM " + TABLE_NAME +
                    " WHERE employee_id = ?";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_NAME +
                    " SET task_id = ?, employee_id = ? " +
                    "WHERE id = ?";

    private static final String CLEAR_TASK_ID_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE task_id = ? AND " +
                    "employee_id = ?";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final Logger LOGGER = Logger.getLogger(TaskParticipationRepository.class.getName());
    private final ConnectionGetter connectionGetter;

    public TaskParticipationRepository(ConnectionGetter connectionGetter) {
        this.connectionGetter = connectionGetter;
    }

    @Override
    public void insert(TaskParticipation entity) throws RepositoryException {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getTaskId());
            statement.setLong(2, entity.getEmployeeId());

            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();

            if (keys.next()) {
                entity.setId(keys.getLong("id"));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка добавления участия в задаче: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<TaskParticipation> findAll() throws RepositoryException {
        List<TaskParticipation> taskParticipation = new ArrayList<>();

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                taskParticipation.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска всех участий в задачах: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return taskParticipation;
    }

    @Override
    public TaskParticipation findById(Long id) throws RepositoryException {
        TaskParticipation taskParticipation = null;

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                taskParticipation = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска участия в задаче по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return taskParticipation;
    }

    public List<TaskParticipation> findByEmployeeId(Long employeeId) throws RepositoryException {
        List<TaskParticipation> taskParticipation = new ArrayList<>();

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMPLOYEE_ID_TEMPLATE)) {
            statement.setLong(1, employeeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    TaskParticipation participation = mapResultSetToEntity(resultSet);
                    taskParticipation.add(participation);
                }
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска участия в задаче по ID сотрудника: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return taskParticipation;
    }

    @Override
    public void update(TaskParticipation updatedEntity) throws RepositoryException {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(3, updatedEntity.getId());
            statement.setLong(1, updatedEntity.getTaskId());
            statement.setLong(2, updatedEntity.getEmployeeId());

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка изменения участия в задаче по ID: " + e.getMessage());

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
            LOGGER.log(Level.SEVERE, "Ошибка удаления участия в задаче по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    public void deleteByEmployeeAndTaskId(Long employeeId, Long taskId) throws RepositoryException {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(CLEAR_TASK_ID_TEMPLATE)) {
            statement.setLong(1, taskId);
            statement.setLong(2, employeeId);

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка изменения сотрудника по ID: " + e.getMessage());

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
