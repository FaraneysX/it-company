package ru.denisov.itcompany.servlet.guest;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.denisov.itcompany.dto.employee.controller.EmployeeRegistrationControllerDto;
import ru.denisov.itcompany.dto.employee.view.EmployeeRegistrationViewDto;
import ru.denisov.itcompany.manager.MapperManager;
import ru.denisov.itcompany.manager.ServiceManager;
import ru.denisov.itcompany.manager.ValidationManager;
import ru.denisov.itcompany.mapper.EmployeeMapper;
import ru.denisov.itcompany.processing.validator.load.LoadValidationResult;
import ru.denisov.itcompany.processing.validator.registration.RegistrationValidator;
import ru.denisov.itcompany.service.EmployeeService;
import ru.denisov.itcompany.utils.JspPathCreator;
import ru.denisov.itcompany.utils.UrlPathGetter;

import java.io.IOException;

import static ru.denisov.itcompany.utils.AttributeGetter.ATTRIBUTE_ERRORS;
import static ru.denisov.itcompany.utils.JspPathGetter.REGISTRATION_JSP;
import static ru.denisov.itcompany.utils.UrlPathGetter.LOGIN_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.REGISTRATION_URL;

@WebServlet(REGISTRATION_URL)
public class RegistrationServlet extends HttpServlet {
    private static EmployeeService service;
    private static EmployeeMapper mapper;
    private static RegistrationValidator registrationValidator;

    private static EmployeeRegistrationViewDto getEmployeeRegistrationViewDto(HttpServletRequest req) {
        return EmployeeRegistrationViewDto.builder()
                .name(req.getParameter("name"))
                .surname(req.getParameter("surname"))
                .birthDate(req.getParameter("birthDate"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .build();
    }

    @Override
    public void init(ServletConfig config) {
        service = ServiceManager.getEmployeeService();
        registrationValidator = ValidationManager.getRegistrationValidator();
        mapper = MapperManager.getEmployeeMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspPathCreator.getJspDefaultPathFormat(REGISTRATION_JSP)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        final EmployeeRegistrationViewDto employeeRegistrationViewDto = getEmployeeRegistrationViewDto(req);
        final LoadValidationResult result = registrationValidator.validate(employeeRegistrationViewDto);

        if (result.isEmpty()) {
            EmployeeRegistrationControllerDto employeeRegistrationControllerDto = mapper.mapToRegistrationController(employeeRegistrationViewDto);
            service.insert(employeeRegistrationControllerDto);
            resp.sendRedirect(UrlPathGetter.getFullPath(LOGIN_URL));
        } else {
            req.setAttribute(ATTRIBUTE_ERRORS, result.getLoadErrors());
            doGet(req, resp);
        }
    }
}
