package ru.denisov.itcompany.servlet.guest;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.denisov.itcompany.utils.UrlPathGetter;

import java.io.IOException;

import static ru.denisov.itcompany.utils.UrlPathGetter.LOGIN_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.LOGOUT_URL;

@WebServlet(LOGOUT_URL)
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        resp.sendRedirect(UrlPathGetter.getFullPath(LOGIN_URL));
    }
}
