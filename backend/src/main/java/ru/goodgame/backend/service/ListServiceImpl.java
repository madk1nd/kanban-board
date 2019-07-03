package ru.goodgame.backend.service;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import lombok.val;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ListServiceImpl implements ListService {

    @Nonnull private static final String BOARD_DB = "boards";
    @Nonnull private final MongoClient client;

    public ListServiceImpl(@Nonnull final MongoClient client) {
        this.client = client;
    }

    @Override
    public void getAllLists(@Nonnull final RoutingContext ctx) {
        val userId = ctx.get("userId");
        val board = ctx.queryParams().get("board");
        val id = new JsonObject().put("$oid", board);
        val elementMatch = new JsonObject().put("_id", id);
        val boards = new JsonObject().put("$elemMatch", elementMatch);
        val fields = new JsonObject()
                .put("_id", 0)
                .put("boards.name", 0)
                .put("boards._id", 0)
                .put("boards", boards);
        val query = new JsonObject().put("_id", userId);
        val findOptions = new FindOptions(
                new JsonObject()
                        .put("fields", fields)
        );
        client.findWithOptions(
                BOARD_DB,
                query,
                findOptions,
                ar -> {
                    if (ar.succeeded()) {
                        val result = ar.result().stream()
                                .flatMap(json -> json.getJsonArray("boards").stream())
                                .filter(o -> o instanceof JsonObject)
                                .map(o -> (JsonObject) o)
                                .filter(json -> json.getJsonArray("lists") != null)
                                .map(json -> json.getJsonArray("lists"))
                                .findFirst()
                                .orElse(new JsonArray());
                        ctx.response()
                                .end(Json.encode(result));
                    } else {
                        ctx.response()
                                .end(ar.cause().getMessage());
                    }
                });
    }

    @Override
    public void add(@Nonnull final RoutingContext ctx) {
        val userId = ctx.get("userId");
        val board = ctx.queryParams().get("board");
        val ordinal = Integer.parseInt(ctx.queryParams().get("ordinal"));
        val title = ctx.queryParams().get("title");
        val listItem = new JsonObject()
                .put("id", UUID.randomUUID().toString())
                .put("ordinal", ordinal)
                .put("title", title);

        val boardsId = new JsonObject().put("$oid", board);
        val query = new JsonObject()
                .put("_id", userId)
                .put("boards._id", boardsId);

        val boardsLists = new JsonObject().put("boards.$.lists", listItem);
        val update = new JsonObject()
                .put("$push", boardsLists);
        client.updateCollection(
                BOARD_DB,
                query,
                update,
                ar -> {
                    if (ar.succeeded()) {
                        ctx.response()
                                .end(listItem.encodePrettily());
                    } else {
                        ctx.response()
                                .end(ar.cause().getMessage());
                    }
                }
        );
    }

    @Override
    public void delete(@Nonnull final RoutingContext ctx) {
        val board = ctx.queryParams().get("board");
        val listId = ctx.queryParams().get("id");
        val id = new JsonObject().put("$oid", board);
        val query = new JsonObject().put("_id", id);
        val lists = new JsonObject().put("id", listId);
        val pull = new JsonObject().put("lists", lists);
        val update = new JsonObject().put("$pull", pull);
        client.updateCollection(
                BOARD_DB,
                query,
                update,
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
    public void update(@Nonnull final RoutingContext ctx) {
        @Nonnull val board = ctx.queryParams().get("board");
        ctx.request().bodyHandler(buffer ->
        {
            val id = new JsonObject().put("$oid", board);
            val query = new JsonObject().put("_id", id);
            val lists = new JsonObject().put("lists", buffer.toJsonArray());
            val update = new JsonObject().put("$set", lists);
            client.updateCollection(
                    BOARD_DB,
                    query,
                    update,
                    ar -> {
                        if (ar.succeeded()) {
                            ctx.response().end();
                        } else {
                            ctx.response().end(ar.cause().getMessage());
                        }
                    }
            );
        });
    }
}
