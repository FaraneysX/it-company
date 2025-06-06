package ru.denisov.itcompany.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.denisov.itcompany.dto.employee.controller.EmployeeControllerDto;
import ru.denisov.itcompany.dto.project.controller.ProjectControllerDto;
import ru.denisov.itcompany.manager.ServiceManager;
import ru.denisov.itcompany.service.EmployeeService;
import ru.denisov.itcompany.service.ProjectService;
import ru.denisov.itcompany.utils.JspPathCreator;

import java.io.IOException;
import java.time.LocalDate;

import static ru.denisov.itcompany.utils.AttributeGetter.ADDED_PROJECT_NAME;
import static ru.denisov.itcompany.utils.AttributeGetter.EMPLOYEE_ATTRIBUTE;
import static ru.denisov.itcompany.utils.JspPathGetter.ADD_PROJECT_JSP;
import static ru.denisov.itcompany.utils.UrlPathGetter.ADD_PROJECT_URL;

@WebServlet(ADD_PROJECT_URL)
public class AddProjectServlet extends HttpServlet {
    private ProjectService service;
    private EmployeeService employeeService;

    @Override
    public void init() {
        service = ServiceManager.getProjectService();
        employeeService = ServiceManager.getEmployeeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspPathCreator.getEmployeePathJspFormat(ADD_PROJECT_JSP)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");

        if (service.existsByName(name)) {
            req.setAttribute("add_project_error", "Проект с таким именем уже существует.");
            req.getRequestDispatcher(JspPathCreator.getEmployeePathJspFormat(ADD_PROJECT_JSP)).forward(req, resp);
            return;
        }

        String date = req.getParameter("startDate");

        if (LocalDate.parse(date).isBefore(LocalDate.now())) {
            req.setAttribute("add_project_error", "Дата старта проекта не может быть раньше текущего времени.");
            req.getRequestDispatcher(JspPathCreator.getEmployeePathJspFormat(ADD_PROJECT_JSP)).forward(req, resp);
            return;
        }

        ProjectControllerDto controller = ProjectControllerDto.builder()
                .name(name)
                .startDate(LocalDate.parse(date))
                .build();

        Long id = service.insertID(controller);

        EmployeeControllerDto employeeControllerDto = (EmployeeControllerDto) req.getSession().getAttribute(EMPLOYEE_ATTRIBUTE);
        EmployeeControllerDto newEmployee = EmployeeControllerDto.builder()
                .id(employeeControllerDto.id())
                .name(employeeControllerDto.name())
                .surname(employeeControllerDto.surname())
                .projectId(id)
                .email(employeeControllerDto.email())
                .birthDate(employeeControllerDto.birthDate())
                .password(employeeControllerDto.password())
                .build();

        employeeService.update(newEmployee);

        req.setAttribute(ADDED_PROJECT_NAME, "Проект добавлен.");
        req.getRequestDispatcher(JspPathCreator.getEmployeePathJspFormat(ADD_PROJECT_JSP)).forward(req, resp);
    }
}
