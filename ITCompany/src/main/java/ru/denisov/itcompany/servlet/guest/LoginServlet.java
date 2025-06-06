package ru.denisov.itcompany.servlet.guest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.denisov.itcompany.dto.employee.controller.EmployeeLoginControllerDto;
import ru.denisov.itcompany.dto.employee.view.EmployeeLoginViewDto;
import ru.denisov.itcompany.manager.MapperManager;
import ru.denisov.itcompany.manager.ServiceManager;
import ru.denisov.itcompany.mapper.EmployeeMapper;
import ru.denisov.itcompany.service.EmployeeService;
import ru.denisov.itcompany.service.login.LoginResult;
import ru.denisov.itcompany.utils.JspPathCreator;
import ru.denisov.itcompany.utils.UrlPathGetter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static ru.denisov.itcompany.utils.AttributeGetter.EMPLOYEE_ATTRIBUTE;
import static ru.denisov.itcompany.utils.AttributeGetter.ERROR_ATTRIBUTE;
import static ru.denisov.itcompany.utils.JspPathGetter.LOGIN_JSP;
import static ru.denisov.itcompany.utils.UrlPathGetter.LOGIN_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.TASKS_LIST_URL;

@WebServlet(LOGIN_URL)
public class LoginServlet extends HttpServlet {
    private static EmployeeService service;
    private static EmployeeMapper mapper;

    @Override
    public void init() {
        service = ServiceManager.getEmployeeService();
        mapper = MapperManager.getEmployeeMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspPathCreator.getJspDefaultPathFormat(LOGIN_JSP)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final EmployeeLoginViewDto employeeLoginViewDto = new EmployeeLoginViewDto(req.getParameter("email"), req.getParameter("password"));
        final EmployeeLoginControllerDto employeeLoginControllerDto = mapper.mapToLoginController(employeeLoginViewDto);
        final LoginResult result;

        try {
            result = service.login(employeeLoginControllerDto);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        if (result.controllerDto().isPresent()) {
            req.getSession().setAttribute(EMPLOYEE_ATTRIBUTE, result.controllerDto().get());
            resp.sendRedirect(UrlPathGetter.getFullPath(TASKS_LIST_URL));
        } else {
            req.setAttribute(ERROR_ATTRIBUTE, result.error());
            req.getRequestDispatcher(JspPathCreator.getJspDefaultPathFormat(LOGIN_JSP)).forward(req, resp);
        }
    }
}
