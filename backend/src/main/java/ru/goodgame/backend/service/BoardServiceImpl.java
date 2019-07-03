package ru.goodgame.backend.service;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.UpdateOptions;
import io.vertx.ext.web.RoutingContext;
import lombok.val;
import org.bson.types.ObjectId;
import ru.goodgame.backend.dto.Board;

import javax.annotation.Nonnull;

import static ru.goodgame.backend.utils.MongoCollections.BOARDS;

public class BoardServiceImpl implements IBoardService {

    @Nonnull private final MongoClient client;

    public BoardServiceImpl(@Nonnull final MongoClient client) {
        this.client = client;
    }

    @Override
    public void getAllBoards(@Nonnull final RoutingContext ctx) {
        val userId = ctx.get("userId");
        val query = new JsonObject().put("_id", userId);
        client.findOne(
                BOARDS,
                query,
                null,
                ar -> {
                    if (ar.succeeded()) {
                        ctx.response()
                                .end(Json.encode(
                                        Board.fromJson(ar.result())
                                ));
                    }
                    else {
                        ctx.response()
                                .end(ar.cause().getMessage());
                    }
                });
    }

    @Override
    public void addBoard(@Nonnull final RoutingContext ctx) {
        val userId = ctx.get("userId");
        ctx.request().bodyHandler(buffer -> {

            val board = new Board(
                    new ObjectId().toString(),
                    buffer.toString()
            );

            val boards = new JsonObject().put("boards", board.toUpdate());

            val query = new JsonObject().put("_id", userId);
            val update = new JsonObject().put("$push", boards);
            val options = new UpdateOptions().setUpsert(true);

            client.updateCollectionWithOptions(
                    BOARDS,
                    query,
                    update,
                    options,
                    ar -> {
                        if (ar.succeeded()) {
                            ctx.response().end(Json.encode(board.toJson()));
                        } else {
                            ctx.response().end(ar.cause().getMessage());
                        }
                    }
            );
        });
    }

    @Override
    public void deleteBoard(@Nonnull final RoutingContext ctx) {
        val userId = ctx.get("userId");
        ctx.request().bodyHandler(buffer -> {
            val id = buffer.toString();
            val board = new Board(id,"");

            val query = new JsonObject().put("_id", userId);
            val boards = new JsonObject()
                    .put("boards", board.toDelete());
            val update = new JsonObject().put(
                    "$pull",
                    boards
            );

            client.updateCollectionWithOptions(
                    BOARDS,
                    query,
                    update,
                    new UpdateOptions(),
                    ar -> {
                        if (ar.succeeded()) {
                            ctx.response().end(id);
                        } else {
                            ctx.response().end(
                                    ar.cause()
                                            .getMessage()
                            );
                        }
                    }
            );
        });
    }
}
