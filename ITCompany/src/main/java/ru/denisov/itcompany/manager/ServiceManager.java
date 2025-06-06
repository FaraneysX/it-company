package ru.denisov.itcompany.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.denisov.itcompany.service.EmployeeService;
import ru.denisov.itcompany.service.ProjectService;
import ru.denisov.itcompany.service.TaskParticipationService;
import ru.denisov.itcompany.service.TaskService;

@UtilityClass
public class ServiceManager {
    @Getter
    private static final EmployeeService employeeService;

    @Getter
    private static final ProjectService projectService;

    @Getter
    private static final TaskService taskService;

    @Getter
    private static final TaskParticipationService taskParticipationService;

    static {
        employeeService = new EmployeeService(RepositoryManager.getEmployeeRepository(), MapperManager.getEmployeeMapper());
        projectService = new ProjectService(RepositoryManager.getProjectRepository(), MapperManager.getProjectMapper());
        taskService = new TaskService(RepositoryManager.getTaskRepository(), MapperManager.getTaskMapper());
        taskParticipationService = new TaskParticipationService(RepositoryManager.getTaskParticipationRepository(), MapperManager.getTaskParticipationMapper());
    }
}
