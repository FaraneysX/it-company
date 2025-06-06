package ru.denisov.itcompany.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.denisov.itcompany.dto.employee.controller.EmployeeControllerDto;
import ru.denisov.itcompany.dto.project.view.ProjectViewDto;
import ru.denisov.itcompany.dto.task.view.TaskViewDto;
import ru.denisov.itcompany.manager.MapperManager;
import ru.denisov.itcompany.manager.ServiceManager;
import ru.denisov.itcompany.mapper.ProjectMapper;
import ru.denisov.itcompany.mapper.TaskMapper;
import ru.denisov.itcompany.service.ProjectService;
import ru.denisov.itcompany.service.TaskService;
import ru.denisov.itcompany.utils.JspPathCreator;

import java.io.IOException;
import java.util.List;

import static ru.denisov.itcompany.utils.AttributeGetter.EMPLOYEE_ATTRIBUTE;
import static ru.denisov.itcompany.utils.JspPathGetter.TASKS_JSP;
import static ru.denisov.itcompany.utils.UrlPathGetter.TASKS_LIST_URL;

@WebServlet(TASKS_LIST_URL)
public class TasksListServlet extends HttpServlet {
    private TaskService service;
    private TaskMapper mapper;
    private ProjectService projectService;
    private ProjectMapper projectMapper;

    @Override
    public void init() {
        service = ServiceManager.getTaskService();
        mapper = MapperManager.getTaskMapper();
        projectService = ServiceManager.getProjectService();
        projectMapper = MapperManager.getProjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final EmployeeControllerDto controllerDto = (EmployeeControllerDto) req.getSession().getAttribute(EMPLOYEE_ATTRIBUTE);
        final List<TaskViewDto> taskViewDtoList = getTasks(controllerDto);
        final List<ProjectViewDto> projectViewDtoList = getProjects(controllerDto);

        req.setAttribute("projectList", projectViewDtoList);
        req.setAttribute("taskList", taskViewDtoList);

        req.getRequestDispatcher(JspPathCreator.getEmployeePathJspFormat(TASKS_JSP)).forward(req, resp);
    }

    private List<ProjectViewDto> getProjects(EmployeeControllerDto controllerDto) {
        return projectService.findByEmployeeId(controllerDto.id()).stream()
                .map(projectMapper::mapToView)
                .toList();
    }

    private List<TaskViewDto> getTasks(EmployeeControllerDto employeeControllerDto) {
        return service.findByEmployeeId(employeeControllerDto.id()).stream()
                .map(mapper::mapToView)
                .toList();
    }
}
