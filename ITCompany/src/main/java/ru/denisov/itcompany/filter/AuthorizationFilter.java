package ru.denisov.itcompany.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.denisov.itcompany.utils.UrlPathGetter;

import java.io.IOException;
import java.util.List;

import static ru.denisov.itcompany.utils.AttributeGetter.EMPLOYEE_ATTRIBUTE;
import static ru.denisov.itcompany.utils.UrlPathGetter.LOGIN_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.REGISTRATION_URL;
import static ru.denisov.itcompany.utils.UrlPathGetter.getFullPath;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {
    private static final List<String> PUBLIC_URI_PATH = List.of(REGISTRATION_URL, LOGIN_URL);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var uri = ((HttpServletRequest) servletRequest).getRequestURI();

        if (isPublishUri(uri) || isEmployeeLogin(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).sendRedirect(getFullPath(LOGIN_URL));
        }
    }

    private boolean isPublishUri(String uri) {
        return PUBLIC_URI_PATH.stream()
                .map(UrlPathGetter::getFullPath).anyMatch(uri::startsWith);
    }

    private boolean isEmployeeLogin(ServletRequest request) {
        var employee = ((HttpServletRequest) request).getSession().getAttribute(EMPLOYEE_ATTRIBUTE);

        return employee != null;
    }
}
