package ru.denisov.itcompany.servlet.user;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.denisov.itcompany.manager.ServiceManager;
import ru.denisov.itcompany.service.ProjectService;

import java.io.IOException;

import static ru.denisov.itcompany.utils.UrlPathGetter.DELETE_PROJECT_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.TASKS_LIST_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.getFullPath;

@WebServlet(DELETE_PROJECT_URL)
public class DeleteProjectServlet extends HttpServlet {
    private ProjectService service;

    @Override
    public void init() {
        service = ServiceManager.getProjectService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("idProject"));

        service.delete(id);

        req.getSession().setAttribute("message", "Проект успешно удален.");
        resp.sendRedirect(getFullPath(TASKS_LIST_URL));
    }
}
