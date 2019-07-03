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
import ru.goodgame.backend.service.BoardServiceImpl;
import ru.goodgame.backend.service.IBoardService;
import ru.goodgame.backend.service.ListService;
import ru.goodgame.backend.service.ListServiceImpl;

import javax.annotation.Nonnull;
import java.time.Instant;

import static ru.goodgame.backend.utils.Routes.AUTH;
import static ru.goodgame.backend.utils.Routes.BOARDS_ADD;
import static ru.goodgame.backend.utils.Routes.BOARDS_ALL;
import static ru.goodgame.backend.utils.Routes.BOARDS_DELETE;
import static ru.goodgame.backend.utils.Routes.LIST_ADD;
import static ru.goodgame.backend.utils.Routes.LIST_DELETE;
import static ru.goodgame.backend.utils.Routes.LIST_GET_ALL;
import static ru.goodgame.backend.utils.Routes.LIST_UPDATE;

@Slf4j
public class BackendApplication extends AbstractVerticle {

    private final static int BACKEND_PORT = 8090;

    private JwtParser parser = Jwts.parser();
    private String secret = "";
    private ListService listService;
    private IBoardService boardService;

    @Override
    public void start(final Future<Void> startFuture) {
        getConfig()
                .map(this::initFields)
                .map(this::buildRouter)
                .compose(this::startHttpServer)
                .setHandler(startFuture);
    }

    @Nonnull
    private Future<Void> startHttpServer(@Nonnull final Router router) {
        val future = Future.<Void>future();
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(BACKEND_PORT, ar -> {
                    if (ar.succeeded()) {
                        log.info("Http server started on port :: {}", BACKEND_PORT);
                        future.complete();
                    } else {
                        Throwable cause = ar.cause();
                        log.error("Http server can't start, cause :: {}", cause.getMessage());
                        future.fail(cause);
                    }
                });
        return future;
    }

    private boolean initFields(@Nonnull final JsonObject config) {
        secret = config.getString("jwt.secret");
        MongoClient shared = MongoClient.createShared(vertx, config);
        listService = new ListServiceImpl(shared);
        boardService = new BoardServiceImpl(shared);
        return config.getBoolean("development");
    }

    @Nonnull
    public Future<JsonObject> getConfig() {
        val future = Future.<JsonObject>future();

        val pathToConfig = config()
                .getString("path", "../config/dev.properties");
        val store = new ConfigStoreOptions()
                .setType("file")
                .setFormat("properties")
                .setConfig(new JsonObject().put("path", pathToConfig));
        val retriever = ConfigRetriever.create(
                vertx,
                new ConfigRetrieverOptions()
                        .addStore(store)
        );

        retriever.getConfig(ar -> {
            if (ar.succeeded()) {
                future.complete(ar.result());
            } else {
                future.fail(ar.cause());
            }
        });
        return future;
    }

    private Router buildRouter(boolean isDev) {
        val router = Router.router(vertx);

        if (isDev) {
            router.route()
                    .handler(CorsHandler.create(".*")
                    .allowedHeader("Content-Type")
                    .allowedHeader("Authorization")
                    .allowedMethod(HttpMethod.DELETE)
                    .allowedMethod(HttpMethod.PUT)
                    .allowedMethod(HttpMethod.POST)
                    .allowedMethod(HttpMethod.GET));
        } else {
            router.route().handler(FaviconHandler.create());
            router.route("/static/*").handler(
                    StaticHandler.create()
                            .setWebRoot("public")
            );
        }

        router.route(AUTH).handler(this::checkToken);

        router.get(BOARDS_ALL).handler(boardService::getAllBoards);
        router.post(BOARDS_ADD).handler(boardService::addBoard);
        router.delete(BOARDS_DELETE).handler(boardService::deleteBoard);

        router.get(LIST_GET_ALL).handler(listService::getAllLists);
        router.post(LIST_ADD).handler(listService::add);
        router.delete(LIST_DELETE).handler(listService::delete);
        router.put(LIST_UPDATE).handler(listService::update);

        return router;
    }

    private void checkToken(@Nonnull final RoutingContext routingContext) {
        val authorization = routingContext.request()
                .getHeader("Authorization");
        if (authorization != null && authorization.length() > 7) {
            val token = authorization.substring(7);
            try {
                val claimsJws = parser
                        .setSigningKey(secret)
                        .parseClaimsJws(token);
                val epochMilli = Instant.now().toEpochMilli();
                val expiresIn = (Long) claimsJws.getBody().get("expiresIn");

                if (expiresIn < epochMilli) {
                    authFailed(routingContext);
                } else {
                    routingContext
                            .put(
                                    "userId",
                                    claimsJws.getBody()
                                            .get("userId")
                            )
                            .next();
                }
            } catch (JwtException e) {
                log.warn("Token invalid :: {}", token);
                authFailed(routingContext);
            }
        } else {
            authFailed(routingContext);
        }
    }

    private void authFailed(@Nonnull final RoutingContext routingContext) {
        routingContext.response()
                .setStatusCode(HttpResponseStatus.UNAUTHORIZED.code())
                .end();
    }
}
