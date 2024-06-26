package ru.denisov.itcompany.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlPathGetter {
    public static final String REGISTRATION_URL = "/registration";
    public static final String LOGIN_URL = "/login";

    public static String getFullPath(String url) {
        return "/ITCompany_war_exploded" + url;
    }
}
