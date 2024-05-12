package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.Position;
import ru.denisov.itcompany.exception.RepositoryException;
import ru.denisov.itcompany.singleton.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PositionRepository implements BaseRepository<Long, Position> {
    private static final String TABLE_NAME = "position";

    private static final String INSERT_TEMPLATE =
            "INSERT INTO " + TABLE_NAME +
                    "(name) " +
                    "VALUES(?)";

    private static final String SELECT_ALL_TEMPLATE =
            "SELECT id, name " +
                    "FROM " + TABLE_NAME;

    private static final String SELECT_BY_ID_TEMPLATE =
            "SELECT * FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final String UPDATE_TEMPLATE =
            "UPDATE " + TABLE_NAME +
                    " SET name = ?" +
                    " WHERE id = ?";

    private static final String DELETE_TEMPLATE =
            "DELETE FROM " + TABLE_NAME +
                    " WHERE id = ?";

    private static final Logger LOGGER = Logger.getLogger(PositionRepository.class.getName());
    private final ConnectionManager connectionManager;

    public PositionRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void insert(Position entity) throws RepositoryException {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE)) {
            statement.setString(1, entity.name());

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка добавления должности: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public List<Position> findAll() throws RepositoryException {
        List<Position> positions = new ArrayList<>();

        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                positions.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска всех должностей: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return positions;
    }

    @Override
    public Optional<Position> findById(Long id) throws RepositoryException {
        Optional<Position> position = Optional.empty();

        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                position = Optional.of(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка поиска должности по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }

        return position;
    }

    @Override
    public void update(Long id, Position updatedEntity) throws RepositoryException {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(2, id);
            statement.setString(1, updatedEntity.name());

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка изменения должности по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        try (Connection connection = connectionManager.get();
             PreparedStatement statement = connection.prepareStatement(DELETE_TEMPLATE)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Ошибка удаления должности по ID: " + e.getMessage());

            throw new RepositoryException(e);
        }
    }

    private Position mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Position(
                resultSet.getLong("id"),
                resultSet.getString("name")
        );
    }
}
