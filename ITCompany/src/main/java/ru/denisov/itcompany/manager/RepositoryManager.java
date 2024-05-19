package ru.denisov.itcompany.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.denisov.itcompany.repository.EmployeeRepository;
import ru.denisov.itcompany.repository.PositionRepository;
import ru.denisov.itcompany.repository.ProjectRepository;
import ru.denisov.itcompany.repository.TaskParticipationRepository;
import ru.denisov.itcompany.repository.TaskRepository;

@UtilityClass
public class RepositoryManager {
    @Getter
    private static final EmployeeRepository employeeRepository;

    @Getter
    private static final PositionRepository positionRepository;

    @Getter
    private static final ProjectRepository projectRepository;

    @Getter
    private static final TaskRepository taskRepository;

    @Getter
    private static final TaskParticipationRepository taskParticipationRepository;

    static {
        employeeRepository = new EmployeeRepository(ConnectionManager.getConnectionGetter());
        positionRepository = new PositionRepository(ConnectionManager.getConnectionGetter());
        projectRepository = new ProjectRepository(ConnectionManager.getConnectionGetter());
        taskRepository = new TaskRepository(ConnectionManager.getConnectionGetter());
        taskParticipationRepository = new TaskParticipationRepository(ConnectionManager.getConnectionGetter());
    }
}
