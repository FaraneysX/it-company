package ru.denisov.itcompany.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlPathGetter {
    public static final String REGISTRATION_URL = "/registration";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String TASKS_LIST_URL = "/tasks/list";
    public static final String ADD_PROJECT_URL = "/project/add";
    public static final String ADD_TASK_URL = "/task/add";
    public static final String DELETE_PROJECT_URL = "/project/delete";
    public static final String DELETE_TASK_URL = "/task/delete";
    public static final String UPDATE_PROJECT_URL = "/project/update";

    public static String getFullPath(String url) {
        return "/ITCompany_war_exploded" + url;
    }
}
