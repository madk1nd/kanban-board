package ru.goodgame.backend.service;

import io.vertx.ext.web.RoutingContext;

import javax.annotation.Nonnull;

public interface IBoardService {
    void getAllBoards(@Nonnull RoutingContext ctx);
    void addBoard(@Nonnull RoutingContext ctx);
    void deleteBoard(@Nonnull RoutingContext routingContext);
}
