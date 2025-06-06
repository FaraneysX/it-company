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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectRepository implements BaseRepository<Long, Project> {
    private static final String TABLE_NAME = "project";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_NAME +
                    "(name, start_date) " +
                    "VALUES(?, ?)";

    private static final String INSERT_WITH_ID_TEMPLATE =
            "INSERT INTO " + TABLE_NAME +
                    "(name, start_date) " +
                    "VALUES(?, ?) " +
                    "RETURNING id";

    private static final String SELECT_ALL_TEMPLATE =
            "SELECT id, name, start_date " +
                    "FROM " + TABLE_NAME;

    private static final String SELECT_BY_ID_TEMPLATE =
            "SELECT id, name, start_date " +
                    "FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final String SELECT_BY_EMPLOYEE_ID_TEMPLATE =
            "SELECT p.id, p.name, p.start_date " +
                    "FROM " + TABLE_NAME + " p " +
                    "JOIN employee e on p.id = e.project_id " +
                    "WHERE e.id = ?";

    private static final String SELECT_BY_NAME_TEMPLATE =
            "SELECT id, name, start_date " +
                    "FROM " + TABLE_NAME +
                    " WHERE name = ?";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_NAME +
                    " SET name = ?, start_date = ? " +
                    "WHERE id = ?";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final String DELETE_BY_NAME_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE name = ?";

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

    public Long insertID(Project entity) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(INSERT_WITH_ID_TEMPLATE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setDate(2, Date.valueOf(entity.getStartDate()));

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

    public List<Project> findByEmployeeId(Long employeeId) {
        List<Project> projects = new ArrayList<>();

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_EMPLOYEE_ID_TEMPLATE)) {
            statement.setLong(1, employeeId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                projects.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска проектов по ID сотрудника: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return projects;
    }

    public Project findByName(String name) {
        Project project = null;

        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME_TEMPLATE)) {
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                project = mapResultSetToEntity(resultSet);
            }
        } catch (SQLException | InterruptedException e) {
            throw new RepositoryException(e);
        }

        return project;
    }

    public boolean existsByName(String name) {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME_TEMPLATE)) {
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException | InterruptedException e) {
            throw new RepositoryException(e);
        }

        return false;
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

    public void delete(String name) throws RepositoryException {
        try (Connection connection = connectionGetter.get();
             PreparedStatement statement = connection.prepareStatement(DELETE_TEMPLATE)) {
            statement.setString(1, name);

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка удаления проекта по названию: " + e.getMessage());

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
