package ru.goodgame.backend.service;

import io.vertx.ext.web.RoutingContext;

import javax.annotation.Nonnull;

public interface ListService {
    void getAllLists(@Nonnull RoutingContext ctx);
    void delete(@Nonnull RoutingContext ctx);
    void add(@Nonnull RoutingContext ctx);
    void update(@Nonnull RoutingContext ctx);
}
