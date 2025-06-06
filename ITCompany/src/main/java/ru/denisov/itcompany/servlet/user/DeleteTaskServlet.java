package ru.denisov.itcompany.servlet.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.denisov.itcompany.manager.ServiceManager;
import ru.denisov.itcompany.service.TaskService;

import java.io.IOException;

import static ru.denisov.itcompany.utils.UrlPathGetter.DELETE_TASK_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.TASKS_LIST_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.getFullPath;

@WebServlet(DELETE_TASK_URL)
public class DeleteTaskServlet extends HttpServlet {
    private TaskService service;

    @Override
    public void init(ServletConfig config) {
        service = ServiceManager.getTaskService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("idTask"));

        service.delete(id);

        req.getSession().setAttribute("message", "Задача успешно удалена.");
        resp.sendRedirect(getFullPath(TASKS_LIST_URL));
    }
}
