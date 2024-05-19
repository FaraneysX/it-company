package ru.denisov.itcompany.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspPathCreator {
    private static final String JSP_DEFAULT_FORMAT = "/WEB-INF/jsp/%s.jsp";
    private static final String JSP_EMPLOYEE_FORMAT = "/WEB-INF/jsp/employee/%s.jsp";

    public static String getJspDefaultFormat(String jsp) {
        return JSP_DEFAULT_FORMAT.formatted(jsp);
    }

    public static String getEmployeeJspFormat(String jsp) {
        return JSP_EMPLOYEE_FORMAT.formatted(jsp);
    }
}
