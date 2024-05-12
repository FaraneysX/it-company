package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.Project;
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
    private Connection connection;

    private static void prepareInsertStatement(Project entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.name());
        statement.setDate(2, Date.valueOf(entity.startDate()));
    }

    @Override
    public void insert(Project entity) throws RepositoryException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE)) {
            prepareInsertStatement(entity, statement);

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка добавления проекта: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Project> findAll() throws RepositoryException {
        List<Project> projects = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                projects.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска всех проектов: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return projects;
    }

    @Override
    public Optional<Project> findById(Long id) throws RepositoryException {
        Optional<Project> project = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                project = Optional.of(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска проекта по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return project;
    }

    @Override
    public void update(Long id, Project updatedEntity) throws RepositoryException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(3, id);
            prepareInsertStatement(updatedEntity, statement);

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка изменения проекта по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_TEMPLATE)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
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
