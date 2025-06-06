package ru.denisov.itcompany.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.denisov.itcompany.entity.Project;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProjectRepositoryTest {
    private static Project project;
    private static List<Project> projects;
    private ConnectionGetter connectionGetter;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private ProjectRepository repository;

    @BeforeAll
    static void init() {
        project = new Project(
                1L,
                "Project1",
                LocalDate.of(2023, 1, 1)
        );

        projects = new ArrayList<>();

        projects.add(new Project(
                1L,
                "Project1",
                LocalDate.of(2023, 1, 1))
        );

        projects.add(new Project(
                2L,
                "Project2",
                LocalDate.of(2023, 2, 1))
        );
    }

    @BeforeEach
    void setUp() {
        connectionGetter = mock(ConnectionGetter.class);
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        repository = new ProjectRepository(connectionGetter);
    }

    @Test
    void insert() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);

        ResultSet generatedKeys = mock(ResultSet.class);

        when(statement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong("id")).thenReturn(1L);

        repository.insert(project);

        verify(statement).setString(eq(1), eq("Project1"));
        verify(statement).setDate(eq(2), eq(Date.valueOf(LocalDate.of(2023, 1, 1))));
        verify(statement).executeUpdate();

        assertEquals(1L, project.getId());
    }

    @Test
    void insertID() throws SQLException, RepositoryException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);

        ResultSet generatedKeys = mock(ResultSet.class);

        when(statement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong("id")).thenReturn(1L);

        Long id = repository.insertID(project);

        verify(statement).setString(eq(1), eq("Project1"));
        verify(statement).setDate(eq(2), eq(Date.valueOf(LocalDate.of(2023, 1, 1))));
        verify(statement).executeUpdate();

        assertEquals(1L, id);
    }

    @Test
    void findAll() throws SQLException, InterruptedException {
        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(resultSet.getLong("id"))
                .thenReturn(1L)
                .thenReturn(2L);

        when(resultSet.getString("name"))
                .thenReturn("Project1")
                .thenReturn("Project2");

        when(resultSet.getDate("start_date"))
                .thenReturn(Date.valueOf(LocalDate.of(2023, 1, 1)))
                .thenReturn(Date.valueOf(LocalDate.of(2023, 2, 1)));

        List<Project> foundProjects = repository.findAll();

        verify(connection).prepareStatement(anyString());
        verify(statement).executeQuery();

        assertEquals(projects, foundProjects);
    }

    @Test
    void findById() throws InterruptedException, SQLException {
        Long projectId = 1L;

        when(connectionGetter.get())
                .thenReturn(connection);
        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);
        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);

        when(resultSet.getLong("id"))
                .thenReturn(project.getId());
        when(resultSet.getString("name"))
                .thenReturn(project.getName());
        when(resultSet.getDate("start_date"))
                .thenReturn(Date.valueOf(project.getStartDate()));

        Project foundProject = repository.findById(projectId);

        verify(connection).prepareStatement(anyString());
        verify(statement).setLong(eq(1), eq(projectId));
        verify(statement).executeQuery();

        assertEquals(project, foundProject);
    }

    @Test
    void findByEmployeeId() throws InterruptedException, SQLException {
        Long employeeId = 1L;

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(resultSet.getLong("id"))
                .thenReturn(1L)
                .thenReturn(2L);

        when(resultSet.getString("name"))
                .thenReturn("Project1")
                .thenReturn("Project2");

        when(resultSet.getDate("start_date"))
                .thenReturn(Date.valueOf(LocalDate.of(2023, 1, 1)))
                .thenReturn(Date.valueOf(LocalDate.of(2023, 2, 1)));

        List<Project> foundProjects = repository.findByEmployeeId(employeeId);

        verify(connection).prepareStatement(anyString());
        verify(statement).setLong(eq(1), eq(employeeId));
        verify(statement).executeQuery();

        assertEquals(projects, foundProjects);
    }

    @Test
    void findByName() throws InterruptedException, SQLException {
        String projectName = "Project1";

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);

        when(resultSet.getLong("id")).thenReturn(project.getId());
        when(resultSet.getString("name")).thenReturn(project.getName());
        when(resultSet.getDate("start_date")).thenReturn(Date.valueOf(project.getStartDate()));

        Project foundProject = repository.findByName(projectName);

        verify(connection).prepareStatement(anyString());
        verify(statement).setString(eq(1), eq(projectName));
        verify(statement).executeQuery();

        assertEquals(project, foundProject);
    }

    @Test
    void existsByName() throws InterruptedException, SQLException {
        String projectName = "Project1";

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);

        boolean exists = repository.existsByName(projectName);

        verify(connection).prepareStatement(anyString());
        verify(statement).setString(eq(1), eq(projectName));
        verify(statement).executeQuery();

        assertTrue(exists);
    }

    @Test
    void update() throws InterruptedException, SQLException {
        Project updatedProject = new Project(
                1L,
                "UpdatedProject",
                LocalDate.of(2023, 3, 1)
        );

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.update(updatedProject);

        verify(connection).prepareStatement(anyString());
        verify(statement).setString(eq(1), eq(updatedProject.getName()));
        verify(statement).setDate(eq(2), eq(Date.valueOf(updatedProject.getStartDate())));
        verify(statement).setLong(eq(3), eq(updatedProject.getId()));
        verify(statement).executeUpdate();
    }

    @Test
    void deleteById() throws InterruptedException, SQLException {
        Long projectId = 1L;

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.delete(projectId);

        verify(connection).prepareStatement(anyString());
        verify(statement).setLong(eq(1), eq(projectId));
        verify(statement).executeUpdate();
    }

    @Test
    void deleteByName() throws InterruptedException, SQLException {
        String projectName = "Project1";

        when(connectionGetter.get()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        repository.delete(projectName);

        verify(connection).prepareStatement(anyString());
        verify(statement).setString(eq(1), eq(projectName));
        verify(statement).executeUpdate();
    }
}
