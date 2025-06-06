package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.Task;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskRepository implements BaseRepository<Long, Task> {
    private static final String TABLE_NAME = "task";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_NAME +
                    "(project_id, name, start_date, end_date) " +
                    "VALUES(?, ?, ?, ?)";

    private static final String INSERT_WITH_ID_TEMPLATE =
            "INSERT INTO " + TABLE_NAME +
                    "(project_id, name, start_date, end_date) " +
                    "VALUES(?, ?, ?, ?) " +
                    "RETURNING id";

    private static final String CLEAR_PROJECT_ID_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE project_id = ?";

    private static final String SELECT_ALL_TEMPLATE =
            "SELECT id, project_id, name, start_date, end_date " +
                    "FROM " + TABLE_NAME;

    private static final String SELECT_BY_ID_TEMPLATE =
            "SELECT id, project_id, name, start_date, end_date " +
                    "FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final String SELECT_BY_EMPLOYEE_ID_TEMPLATE =
            "SELECT t.id, t.project_id, t.name, t.start_date, t.end_date " +
                    "FROM " + TABLE_NAME + " t " +
                    "JOIN task_participation tp ON t.id = tp.task_id " +
                    "WHERE tp.employee_id = ?";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_NAME +
                    " SET name = ?, start_date = ?, end_date = ? WHERE id = ?";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final Logger LOGGER = Logger.getLogger(TaskRepository.class.getName());
    private final ConnectionGetter connectionGetter;

    public TaskRepository(ConnectionGetter connectionGetter) {
        this.connectionGetter = connectionGetter;
    }

    @Override
    public void insert(Task entity) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getProjectId());
            statement.setString(2, entity.getName());
            statement.setDate(3, Date.valueOf(entity.getStartDate()));
            statement.setDate(4, Date.valueOf(entity.getEndDate()));

            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();

            if (keys.next()) {
                entity.setId(keys.getLong("id"));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка добавления задачи: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    public Long insertID(Task entity) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(INSERT_WITH_ID_TEMPLATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getProjectId());
            statement.setString(2, entity.getName());
            statement.setDate(3, Date.valueOf(entity.getStartDate()));
            statement.setDate(4, Date.valueOf(entity.getEndDate()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong("id");
                } else {
                    throw new RepositoryException("Создание задачи не удалось, ID не был получен.");
                }
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка добавления задачи: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tasks.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска всех задач: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return tasks;
    }

    @Override
    public Task findById(Long id) {
        Task task = null;

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                task = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска задачи по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return task;
    }

    public List<Task> findByEmployeeId(Long employeeId) {
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMPLOYEE_ID_TEMPLATE)) {
            statement.setLong(1, employeeId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tasks.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска задачи по ID сотрудника: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return tasks;
    }

    @Override
    public void update(Task updatedEntity) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(4, updatedEntity.getId());
            statement.setString(1, updatedEntity.getName());
            statement.setDate(2, Date.valueOf(updatedEntity.getStartDate()));
            statement.setDate(3, Date.valueOf(updatedEntity.getEndDate()));

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка обновления задачи по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    public void deleteByProjectId(Long projectId) throws RepositoryException {
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
    public void delete(Long id) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(DELETE_TEMPLATE)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка удаления задачи по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    private Task mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Task(
                resultSet.getLong("id"),
                resultSet.getLong("project_id"),
                resultSet.getString("name"),
                resultSet.getDate("start_date").toLocalDate(),
                resultSet.getDate("end_date").toLocalDate()
        );
    }
}
