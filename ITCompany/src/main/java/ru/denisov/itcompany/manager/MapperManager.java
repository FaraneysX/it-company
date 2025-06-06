package ru.denisov.itcompany.manager;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import ru.denisov.itcompany.mapper.EmployeeMapper;
import ru.denisov.itcompany.mapper.ProjectMapper;
import ru.denisov.itcompany.mapper.TaskMapper;
import ru.denisov.itcompany.mapper.TaskParticipationMapper;

@UtilityClass
public class MapperManager {
    @Getter
    private static final EmployeeMapper employeeMapper;

    @Getter
    private static final ProjectMapper projectMapper;

    @Getter
    private static final TaskMapper taskMapper;

    @Getter
    private static final TaskParticipationMapper taskParticipationMapper;

    static {
        employeeMapper = new EmployeeMapper();
        projectMapper = new ProjectMapper();
        taskMapper = new TaskMapper();
        taskParticipationMapper = new TaskParticipationMapper();
    }
}
