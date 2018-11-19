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

    public BoardServiceImpl(@Nonnull MongoClient client) {
        this.client = client;
    }

    @Override
    public void getAllBoards(@Nonnull RoutingContext ctx) {
        @Nonnull val userId = ctx.get("userId");
        @Nonnull val query = new JsonObject().put("_id", userId);
        client.findOne(BOARDS, query, null,
                ar -> {
                    if (ar.succeeded()) ctx.response().end(Json.encode(Board.fromJson(ar.result())));
                    else ctx.response().end(ar.cause().getMessage());
                });

    }

    @Override
    public void addBoard(@Nonnull RoutingContext ctx) {
        @Nonnull val userId = ctx.get("userId");
        ctx.request().bodyHandler(buffer -> {

            @Nonnull val board = new Board(
                    new ObjectId().toString(),
                    buffer.toString()
            );

            @Nonnull val query = new JsonObject().put("_id", userId);

            @Nonnull val update = new JsonObject().put("$push", new JsonObject().put("boards", board.toUpdate()));
            @Nonnull val options = new UpdateOptions().setUpsert(true);

            client.updateCollectionWithOptions(BOARDS, query, update, options,
                    ar -> {
                        if (ar.succeeded()) ctx.response().end(Json.encode(board.toJson()));
                        else ctx.response().end(ar.cause().getMessage());
                    }
            );
        });
    }

    @Override
    public void deleteBoard(@Nonnull RoutingContext ctx) {
        String userId = ctx.get("userId");
        System.out.println(userId);
        ctx.request().bodyHandler(buffer -> {
            @Nonnull val id = buffer.toString();
            @Nonnull val board = new Board(id,"");

            @Nonnull val query = new JsonObject().put("_id", userId);
            @Nonnull val update = new JsonObject().put("$pull", new JsonObject().put("boards", board.toDelete()));

            client.updateCollectionWithOptions(BOARDS, query, update, new UpdateOptions(),
                    ar -> {
                        if (ar.succeeded()) ctx.response().end(id);
                        else ctx.response().end(ar.cause().getMessage());
                    }
            );
        });
    }
}
