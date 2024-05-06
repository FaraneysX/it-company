package ru.denisov.itcompany.repository;

import ru.denisov.itcompany.entity.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PositionRepository implements BaseRepository<Long, Position> {
    private static final String TABLE_NAME = "position";
    private static final String INSERT_TEMPLATE = "INSERT INTO " + TABLE_NAME + "(name) VALUES(?)";
    private static final String SELECT_ALL_TEMPLATE = "SELECT * FROM " + TABLE_NAME;
    private static final String SELECT_BY_ID_TEMPLATE = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String UPDATE_TEMPLATE = "UPDATE " + TABLE_NAME + " SET name = ? WHERE id = ?";
    private static final String DELETE_TEMPLATE = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

    private Connection connection;

    @Override
    public void insert(Position entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_TEMPLATE)) {
            statement.setString(1, entity.name());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Position> findAll() {
        List<Position> positions = new LinkedList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TEMPLATE)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                positions.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return positions;
    }

    @Override
    public Optional<Position> findById(Long id) {
        Optional<Position> position = Optional.empty();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_TEMPLATE)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                position = Optional.of(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return position;
    }

    @Override
    public void update(Long id, Position updatedEntity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TEMPLATE)) {
            statement.setLong(2, id);
            statement.setString(1, updatedEntity.name());

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

    private Position mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Position(
                resultSet.getLong("id"),
                resultSet.getString("name")
        );
    }
}
