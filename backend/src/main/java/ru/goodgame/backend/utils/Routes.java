package ru.goodgame.backend.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Routes {
    public final String AUTH = "/api/*";
    public final String LIST_GET_ALL = "/api/list/all";
    public final String LIST_ADD = "/api/list/add";
    public final String LIST_DELETE = "/api/list/delete";
    public final String LIST_UPDATE = "/api/list/update";
}
