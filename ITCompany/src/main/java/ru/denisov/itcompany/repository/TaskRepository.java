package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.Task;
import ru.denisov.itcompany.exception.RepositoryException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskRepository implements BaseRepository<Long, Task> {
    private static final String TABLE_NAME = "task";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_NAME +
                    "(project_id, name, start_date, end_date) " +
                    "VALUES(?, ?, ?, ?)";

    private static final String SELECT_ALL_TEMPLATE =
            "SELECT id, project_id, name, start_date, end_date " +
                    "FROM " + TABLE_NAME;

    private static final String SELECT_BY_ID_TEMPLATE =
            "SELECT * FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_NAME +
                    " SET name = ?, start_date = ?, end_date = ? WHERE id = ?";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final Logger LOGGER = Logger.getLogger(TaskRepository.class.getName());
    private Connection connection;

    @Override
    public void insert(Task entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE)) {
            statement.setLong(1, entity.projectId());
            statement.setString(2, entity.name());
            statement.setDate(3, Date.valueOf(entity.startDate()));
            statement.setDate(4, Date.valueOf(entity.endDate()));

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка добавления задачи: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tasks.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска всех задач: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return tasks;
    }

    @Override
    public Optional<Task> findById(Long id) {
        Optional<Task> task = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                task = Optional.of(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска задачи по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return task;
    }

    @Override
    public void update(Long id, Task updatedEntity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(4, id);
            statement.setString(1, updatedEntity.name());
            statement.setDate(2, Date.valueOf(updatedEntity.startDate()));
            statement.setDate(3, Date.valueOf(updatedEntity.endDate()));

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка обновления задачи по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_TEMPLATE)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
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