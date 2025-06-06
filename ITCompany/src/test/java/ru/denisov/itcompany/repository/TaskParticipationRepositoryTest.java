package ru.denisov.itcompany.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskParticipationRepositoryTest {
    private TaskParticipation taskParticipation;
    private ConnectionGetter connectionGetter;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private TaskParticipationRepository repository;

    @BeforeEach
    void setUp() {
        connectionGetter = mock(ConnectionGetter.class);
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        repository = new TaskParticipationRepository(connectionGetter);

        taskParticipation = new TaskParticipation(1L, 1L, 1L);
    }

    @Test
    void insert() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);

        ResultSet generatedKeys = mock(ResultSet.class);

        when(statement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong("id")).thenReturn(1L);

        repository.insert(taskParticipation);

        verify(statement).setLong(1, taskParticipation.getTaskId());
        verify(statement).setLong(2, taskParticipation.getEmployeeId());
        verify(statement).executeUpdate();

        assertEquals(1L, taskParticipation.getId());
    }

    @Test
    void findAll() throws SQLException, InterruptedException, RepositoryException {
        List<TaskParticipation> expectedTaskParticipation = new ArrayList<>();
        expectedTaskParticipation.add(taskParticipation);

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(taskParticipation.getId());
        when(resultSet.getLong("task_id")).thenReturn(taskParticipation.getTaskId());
        when(resultSet.getLong("employee_id")).thenReturn(taskParticipation.getEmployeeId());

        List<TaskParticipation> foundTaskParticipation = repository.findAll();

        verify(statement).executeQuery();
        assertEquals(expectedTaskParticipation, foundTaskParticipation);
    }

    @Test
    void findById() throws SQLException, InterruptedException, RepositoryException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(taskParticipation.getId());
        when(resultSet.getLong("task_id")).thenReturn(taskParticipation.getTaskId());
        when(resultSet.getLong("employee_id")).thenReturn(taskParticipation.getEmployeeId());

        TaskParticipation foundTaskParticipation = repository.findById(taskParticipation.getId());

        verify(statement).setLong(1, taskParticipation.getId());
        verify(statement).executeQuery();
        assertEquals(taskParticipation, foundTaskParticipation);
    }

    @Test
    void findByEmployeeId() throws SQLException, InterruptedException, RepositoryException {
        List<TaskParticipation> expectedTaskParticipation = new ArrayList<>();
        expectedTaskParticipation.add(taskParticipation);

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(taskParticipation.getId());
        when(resultSet.getLong("task_id")).thenReturn(taskParticipation.getTaskId());
        when(resultSet.getLong("employee_id")).thenReturn(taskParticipation.getEmployeeId());

        List<TaskParticipation> foundTaskParticipation = repository.findByEmployeeId(taskParticipation.getEmployeeId());

        verify(statement).setLong(1, taskParticipation.getEmployeeId());
        verify(statement).executeQuery();
        assertEquals(expectedTaskParticipation, foundTaskParticipation);
    }

    @Test
    void update() throws SQLException, RepositoryException, InterruptedException {
        TaskParticipation updatedTaskParticipation = new TaskParticipation(1L, 2L, 2L);

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.update(updatedTaskParticipation);

        verify(statement).setLong(1, updatedTaskParticipation.getTaskId());
        verify(statement).setLong(2, updatedTaskParticipation.getEmployeeId());
        verify(statement).setLong(3, updatedTaskParticipation.getId());
        verify(statement).executeUpdate();
    }

    @Test
    void delete() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.delete(taskParticipation.getId());

        verify(statement).setLong(1, taskParticipation.getId());
        verify(statement).executeUpdate();
    }

    @Test
    void deleteByEmployeeAndTaskId() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.deleteByEmployeeAndTaskId(taskParticipation.getEmployeeId(), taskParticipation.getTaskId());

        verify(statement).setLong(1, taskParticipation.getTaskId());
        verify(statement).setLong(2, taskParticipation.getEmployeeId());
        verify(statement).executeUpdate();
    }
}
