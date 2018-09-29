package ru.goodgame.backend.service;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import lombok.val;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ListServiceImpl implements ListService {

    @Nonnull
    private static final String BOARD_DB = "boards";
    @Nonnull
    private final MongoClient client;

    public ListServiceImpl(@Nonnull MongoClient client) {
        this.client = client;
    }

    @Override
    public void getAllLists(@Nonnull RoutingContext ctx) {
        @Nonnull val board = ctx.queryParams().get("board");
        client.find(BOARD_DB, new JsonObject().put("_id", new JsonObject().put("$oid", board)), ar -> {
            if (ar.succeeded()) {
                ctx.response().end(Json.encodePrettily(ar.result()));
            } else {
                ctx.response().end(ar.cause().getMessage());
            }
        });
    }

    @Override
    public void add(@Nonnull RoutingContext ctx) {
        @Nonnull val board = ctx.queryParams().get("board");
        @Nonnull val ordinal = Integer.parseInt(ctx.queryParams().get("ordinal"));
        @Nonnull val title = ctx.queryParams().get("title");
        @Nonnull val listItem = new JsonObject()
                .put("id", UUID.randomUUID().toString())
                .put("ordinal", ordinal)
                .put("title", title);
        client.updateCollection(
                BOARD_DB,
                new JsonObject().put("_id", new JsonObject().put("$oid", board)),
                new JsonObject().put("$push", new JsonObject().put("lists", listItem)),
                ar -> {
                    if (ar.succeeded()) {
                        ctx.response().end(listItem.encodePrettily());
                    } else {
                        ctx.response().end(ar.cause().getMessage());
                    }
                }
        );
    }

    @Override
    public void delete(@Nonnull RoutingContext ctx) {
        @Nonnull val board = ctx.queryParams().get("board");
        @Nonnull val listId = ctx.queryParams().get("id");
        client.updateCollection(
                BOARD_DB,
                new JsonObject().put("_id", new JsonObject().put("$oid", board)),
                new JsonObject().put("$pull", new JsonObject().put("lists",
                        new JsonObject().put("id", listId))),
                ar -> {
                    if (ar.succeeded()) {
                        ctx.response().end();
                    } else {
                        ctx.response().end(ar.cause().getMessage());
                    }
                }
        );
    }

    @Override
    public void update(@Nonnull RoutingContext ctx) {
        @Nonnull val board = ctx.queryParams().get("board");
        ctx.request().bodyHandler(buffer ->
                client.updateCollection(
                        BOARD_DB,
                        new JsonObject().put("_id", new JsonObject().put("$oid", board)),
                        new JsonObject().put("$set", new JsonObject().put("lists", buffer.toJsonArray())),
                        ar -> {
                            if (ar.succeeded()) {
                                ctx.response().end();
                            } else {
                                ctx.response().end(ar.cause().getMessage());
                            }
                        }
                ));
    }
}
