package ru.denisov.itcompany.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.denisov.itcompany.dto.employee.controller.EmployeeControllerDto;
import ru.denisov.itcompany.dto.task.controller.TaskControllerDto;
import ru.denisov.itcompany.dto.taskparticipation.controller.TaskParticipationControllerDto;
import ru.denisov.itcompany.manager.ServiceManager;
import ru.denisov.itcompany.service.ProjectService;
import ru.denisov.itcompany.service.TaskParticipationService;
import ru.denisov.itcompany.service.TaskService;
import ru.denisov.itcompany.utils.JspPathCreator;

import java.io.IOException;
import java.time.LocalDate;

import static ru.denisov.itcompany.utils.AttributeGetter.ADDED_TASK_NAME;
import static ru.denisov.itcompany.utils.AttributeGetter.EMPLOYEE_ATTRIBUTE;
import static ru.denisov.itcompany.utils.JspPathGetter.ADD_TASK_JSP;
import static ru.denisov.itcompany.utils.UrlPathGetter.ADD_TASK_URL;

@WebServlet(ADD_TASK_URL)
public class AddTaskServlet extends HttpServlet {
    private TaskService service;
    private ProjectService projectService;
    private TaskParticipationService taskParticipationService;

    @Override
    public void init() throws ServletException {
        service = ServiceManager.getTaskService();
        projectService = ServiceManager.getProjectService();
        taskParticipationService = ServiceManager.getTaskParticipationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspPathCreator.getEmployeePathJspFormat(ADD_TASK_JSP)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectName = req.getParameter("projectName");

        if (!projectService.existsByName(projectName)) {
            req.setAttribute("add_task_error", "Проекта с таким именем не существует.");
            req.getRequestDispatcher(JspPathCreator.getEmployeePathJspFormat(ADD_TASK_JSP)).forward(req, resp);
            return;
        }

        Long employeeId = ((EmployeeControllerDto) req.getSession().getAttribute(EMPLOYEE_ATTRIBUTE)).id();
        String name = req.getParameter("name");
        LocalDate startDate = LocalDate.parse(req.getParameter("startDate"));
        LocalDate endDate = LocalDate.parse(req.getParameter("endDate"));

        TaskControllerDto taskControllerDto = TaskControllerDto.builder()
                .name(name)
                .projectId(projectService.findByName(projectName).id())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Long id = service.insertID(taskControllerDto);

        TaskParticipationControllerDto taskParticipationControllerDto = TaskParticipationControllerDto.builder()
                .taskId(id)
                .employeeId(employeeId)
                .build();

        taskParticipationService.insert(taskParticipationControllerDto);

        req.setAttribute(ADDED_TASK_NAME, "Задача добавлена.");
        req.getRequestDispatcher(JspPathCreator.getEmployeePathJspFormat(ADD_TASK_JSP)).forward(req, resp);
    }
}
