package ru.denisov.itcompany.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.denisov.itcompany.entity.Task;
import ru.denisov.itcompany.exception.RepositoryException;
import ru.denisov.itcompany.processing.ConnectionGetter;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskRepositoryTest {
    private Task task;
    private ConnectionGetter connectionGetter;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private TaskRepository repository;

    @BeforeEach
    void setUp() {
        connectionGetter = mock(ConnectionGetter.class);
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        repository = new TaskRepository(connectionGetter);

        task = new Task(1L, 1L, "Task Name", LocalDate.now(), LocalDate.now().plusDays(1));
    }

    @Test
    void insert() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);

        ResultSet generatedKeys = mock(ResultSet.class);

        when(statement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong("id")).thenReturn(1L);

        repository.insert(task);

        verify(statement).setLong(1, task.getProjectId());
        verify(statement).setString(2, task.getName());
        verify(statement).setDate(3, Date.valueOf(task.getStartDate()));
        verify(statement).setDate(4, Date.valueOf(task.getEndDate()));
        verify(statement).executeUpdate();

        assertEquals(1L, task.getId());
    }

    @Test
    void insertID() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);

        ResultSet generatedKeys = mock(ResultSet.class);

        when(statement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong("id")).thenReturn(1L);

        Long id = repository.insertID(task);

        verify(statement).setLong(1, task.getProjectId());
        verify(statement).setString(2, task.getName());
        verify(statement).setDate(3, Date.valueOf(task.getStartDate()));
        verify(statement).setDate(4, Date.valueOf(task.getEndDate()));
        verify(statement).executeUpdate();

        assertEquals(1L, id);
    }

    @Test
    void findAll() throws SQLException, InterruptedException, RepositoryException {
        List<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(task);

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(task.getId());
        when(resultSet.getLong("project_id")).thenReturn(task.getProjectId());
        when(resultSet.getString("name")).thenReturn(task.getName());
        when(resultSet.getDate("start_date")).thenReturn(Date.valueOf(task.getStartDate()));
        when(resultSet.getDate("end_date")).thenReturn(Date.valueOf(task.getEndDate()));

        List<Task> foundTasks = repository.findAll();

        verify(statement).executeQuery();
        assertEquals(expectedTasks, foundTasks);
    }

    @Test
    void findById() throws SQLException, InterruptedException, RepositoryException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(task.getId());
        when(resultSet.getLong("project_id")).thenReturn(task.getProjectId());
        when(resultSet.getString("name")).thenReturn(task.getName());
        when(resultSet.getDate("start_date")).thenReturn(Date.valueOf(task.getStartDate()));
        when(resultSet.getDate("end_date")).thenReturn(Date.valueOf(task.getEndDate()));

        Task foundTask = repository.findById(task.getId());

        verify(statement).setLong(1, task.getId());
        verify(statement).executeQuery();
        assertEquals(task, foundTask);
    }

    @Test
    void findByEmployeeId() throws SQLException, InterruptedException, RepositoryException {
        List<Task> expectedTasks = new ArrayList<>();
        expectedTasks.add(task);

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(task.getId());
        when(resultSet.getLong("project_id")).thenReturn(task.getProjectId());
        when(resultSet.getString("name")).thenReturn(task.getName());
        when(resultSet.getDate("start_date")).thenReturn(Date.valueOf(task.getStartDate()));
        when(resultSet.getDate("end_date")).thenReturn(Date.valueOf(task.getEndDate()));

        List<Task> foundTasks = repository.findByEmployeeId(1L);

        verify(statement).setLong(1, 1L);
        verify(statement).executeQuery();
        assertEquals(expectedTasks, foundTasks);
    }

    @Test
    void update() throws SQLException, RepositoryException, InterruptedException {
        Task updatedTask = new Task(1L, 1L, "Updated Task", LocalDate.now(), LocalDate.now().plusDays(1));

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.update(updatedTask);

        verify(statement).setString(1, updatedTask.getName());
        verify(statement).setDate(2, Date.valueOf(updatedTask.getStartDate()));
        verify(statement).setDate(3, Date.valueOf(updatedTask.getEndDate()));
        verify(statement).setLong(4, updatedTask.getId());
        verify(statement).executeUpdate();
    }

    @Test
    void delete() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.delete(task.getId());

        verify(statement).setLong(1, task.getId());
        verify(statement).executeUpdate();
    }

    @Test
    void deleteByProjectId() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.deleteByProjectId(task.getProjectId());

        verify(statement).setLong(1, task.getProjectId());
        verify(statement).executeUpdate();
    }
}
