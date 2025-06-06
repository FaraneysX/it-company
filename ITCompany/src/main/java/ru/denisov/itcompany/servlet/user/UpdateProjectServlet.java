package ru.denisov.itcompany.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.denisov.itcompany.dto.project.controller.ProjectControllerDto;
import ru.denisov.itcompany.dto.project.view.ProjectViewDto;
import ru.denisov.itcompany.manager.MapperManager;
import ru.denisov.itcompany.manager.ServiceManager;
import ru.denisov.itcompany.mapper.ProjectMapper;
import ru.denisov.itcompany.service.ProjectService;
import ru.denisov.itcompany.utils.JspPathCreator;

import java.io.IOException;
import java.time.LocalDate;

import static ru.denisov.itcompany.utils.JspPathGetter.UPDATE_PROJECT_JSP;
import static ru.denisov.itcompany.utils.UrlPathGetter.TASKS_LIST_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.UPDATE_PROJECT_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.getFullPath;

@WebServlet(UPDATE_PROJECT_URL)
public class UpdateProjectServlet extends HttpServlet {
    private ProjectService service;
    private ProjectMapper mapper;

    @Override
    public void init(ServletConfig config) {
        service = ServiceManager.getProjectService();
        mapper = MapperManager.getProjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("idProject"));
        ProjectViewDto viewDto = mapper.mapToView(service.findById(id));

        req.setAttribute("project", viewDto);
        req.getRequestDispatcher(JspPathCreator.getEmployeePathJspFormat(UPDATE_PROJECT_JSP)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("idProject"));
        ProjectControllerDto controllerDto = service.findById(id);

        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        ProjectControllerDto newProject = ProjectControllerDto.builder()
                .id(controllerDto.id())
                .name(name)
                .startDate(LocalDate.parse(startDate))
                .build();

        service.update(newProject);

        req.getSession().setAttribute("message", "Проект обновлен");
        resp.sendRedirect(getFullPath(TASKS_LIST_URL));
    }
}
