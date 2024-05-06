package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.Project;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ProjectRepository implements BaseRepository<Long, Project> {
    private static final String TABLE_NAME = "project";
    private static final String INSERT_TEMPLATE = "INSERT INTO " + TABLE_NAME + "(name, start_date) VALUES(?, ?)";
    private static final String SELECT_ALL_TEMPLATE = "SELECT * FROM " + TABLE_NAME;
    private static final String SELECT_BY_ID_TEMPLATE = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String UPDATE_TEMPLATE = "UPDATE " + TABLE_NAME + " SET name = ?, start_date = ? WHERE id = ?";
    private static final String DELETE_TEMPLATE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

    private Connection connection;

    private static void prepareInsertStatement(Project entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.name());
        statement.setDate(2, Date.valueOf(entity.startDate()));
    }

    @Override
    public void insert(Project entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE)) {
            prepareInsertStatement(entity, statement);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Project> findAll() {
        List<Project> projects = new LinkedList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                projects.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return projects;
    }

    @Override
    public Optional<Project> findById(Long id) {
        Optional<Project> project = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                project = Optional.of(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return project;
    }

    @Override
    public void update(Long id, Project updatedEntity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(3, id);
            prepareInsertStatement(updatedEntity, statement);

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

    private Project mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Project(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("start_date").toLocalDate()
        );
    }
}
