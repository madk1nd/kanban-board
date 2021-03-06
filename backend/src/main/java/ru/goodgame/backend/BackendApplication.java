package ru.goodgame.backend;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.goodgame.backend.service.ListService;
import ru.goodgame.backend.service.ListServiceImpl;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class BackendApplication extends AbstractVerticle {

    private final static int BACKEND_PORT = 8090;

    private JwtParser parser = Jwts.parser();
    private String secret = "";
    private ListService listService;

    @Override
    public void start(Future<Void> startFuture) {
        getConfig()
                .thenApply(this::initFields)
                .thenApply(this::buildRouter)
                .thenAccept(this::startHttpServer)
                .thenRun(startFuture::complete)
                .exceptionally(throwable -> failure(throwable, startFuture));
    }

    public Void failure(Throwable throwable, Future<Void> future) {
        log.info("Can't start backend application, cause :: {}", throwable.getMessage());
        future.fail("Can't start backend application");
        return null;
    }

    private CompletableFuture<Void> startHttpServer(@Nonnull Router router) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(BACKEND_PORT, ar -> {
                    if (ar.succeeded()) {
                        log.info("Http server started on port :: {}", BACKEND_PORT);
                        future.complete(null);
                    } else {
                        Throwable cause = ar.cause();
                        log.error("Http server can't start, cause :: {}", cause.getMessage());
                        future.completeExceptionally(cause);
                    }
                });
        return future;
    }

    private boolean initFields(@Nonnull JsonObject config) {
        secret = config.getString("jwt.secret");
        listService = new ListServiceImpl(MongoClient.createShared(vertx, config));
        return config.getBoolean("development");
    }

    public CompletableFuture<JsonObject> getConfig() {
        @Nonnull CompletableFuture<JsonObject> future = new CompletableFuture<>();

        @Nonnull val pathToConfig = config().getString("path", "../config/dev.properties");
        @Nonnull val store = new ConfigStoreOptions()
                .setType("file")
                .setFormat("properties")
                .setConfig(new JsonObject().put("path", pathToConfig));
        @Nonnull val retriever = ConfigRetriever.create(
                vertx,
                new ConfigRetrieverOptions().addStore(store)
        );

        retriever.getConfig(ar -> {
            if (ar.succeeded()) {
                future.complete(ar.result());
            } else {
                future.completeExceptionally(new RuntimeException("Can't get Vertx config"));
            }
        });
        return future;
    }

    private Router buildRouter(boolean isDev) {
        Router router = Router.router(vertx);

        if (isDev) {
            router.route().handler(CorsHandler.create(".*")
                    .allowedHeader("Content-Type")
                    .allowedHeader("Authorization")
                    .allowedMethod(HttpMethod.DELETE)
                    .allowedMethod(HttpMethod.PUT)
                    .allowedMethod(HttpMethod.POST)
                    .allowedMethod(HttpMethod.GET));
        } else {
            router.route().handler(FaviconHandler.create());
            router.route("/static/*").handler(StaticHandler.create().setWebRoot("public"));
        }

        router.route("/api/*").handler(this::checkToken);
        router.get("/api/list/all").handler(listService::getAllLists);
        router.post("/api/list/add").handler(listService::add);
        router.delete("/api/list/delete").handler(listService::delete);
        router.put("/api/list/update").handler(listService::update);

        return router;
    }

    private void checkToken(@Nonnull RoutingContext routingContext) {
        if (tokenInvalid(routingContext)) {
            routingContext.response()
                    .setStatusCode(HttpResponseStatus.UNAUTHORIZED.code())
                    .end();
        } else {
            routingContext.next();
        }
    }

    private boolean tokenInvalid(RoutingContext routingContext) {
        String authorization = routingContext.request().getHeader("Authorization");
        return authorization == null || invalid(authorization.substring(7));
    }

    private boolean invalid(@Nonnull String token) {
        @Nonnull val now = Instant.now();
        try {
            @Nonnull val claimsJws = parser.setSigningKey(secret).parseClaimsJws(token);
            return ((Long) claimsJws.getBody().get("expiresIn")) < now.toEpochMilli();
        } catch (JwtException e) {
            log.warn("Token invalid :: {}", token);
            return true;
        }
    }
}
