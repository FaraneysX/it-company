package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.Project;
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

public class ProjectRepository implements BaseRepository<Long, Project> {
    private static final String TABLE_NAME = "project";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_NAME +
                    "(name, start_date) " +
                    "VALUES(?, ?)";

    private static final String SELECT_ALL_TEMPLATE =
            "SELECT id, name, start_date " +
                    "FROM " + TABLE_NAME;

    private static final String SELECT_BY_ID_TEMPLATE =
            "SELECT * FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_NAME +
                    " SET name = ?, start_date = ? " +
                    "WHERE id = ?";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final Logger LOGGER = Logger.getLogger(ProjectRepository.class.getName());
    private final ConnectionGetter connectionGetter;

    public ProjectRepository(ConnectionGetter connectionGetter) {
        this.connectionGetter = connectionGetter;
    }

    private static void prepareInsertStatement(Project entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setDate(2, Date.valueOf(entity.getStartDate()));
    }

    @Override
    public void insert(Project entity) throws RepositoryException {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE, Statement.RETURN_GENERATED_KEYS)) {
            prepareInsertStatement(entity, statement);

            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();

            if (keys.next()) {
                entity.setId(keys.getLong("id"));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка добавления проекта: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Project> findAll() throws RepositoryException {
        List<Project> projects = new ArrayList<>();

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                projects.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска всех проектов: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return projects;
    }

    @Override
    public Project findById(Long id) throws RepositoryException {
        Project project = null;

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                project = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска проекта по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return project;
    }

    @Override
    public void update(Project updatedEntity) throws RepositoryException {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(3, updatedEntity.getId());
            prepareInsertStatement(updatedEntity, statement);

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка изменения проекта по ID: " + e.getMessage());

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
            LOGGER.log(Level.SEVERE, "Ошибка удаления проекта по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    private Project mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Project(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("start_date").toLocalDate()
        );
    }
}
