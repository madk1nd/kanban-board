package ru.goodgame.backend;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.ParsedHeaderValues;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.val;
import ru.goodgame.backend.service.ListService;
import ru.goodgame.backend.service.ListServiceImpl;

import javax.annotation.Nonnull;
import java.time.Instant;


public class BackendApplication extends AbstractVerticle {

//    TODO: move client to service classes (inject in constructor of service only config)
    private MongoClient client;
    private JwtParser parser = Jwts.parser();
    private String secret = "F18718B98349C7D97AE3BE6717D02DAFD3AAEB38EB218D74F0F64DEAA6DC481A88A2DD058BC6EDD5888B78A54CACEA2F85C8C24161FA58D5386E450102F73F2A";
    private ListService listService;

    @Override
    public void start(Future<Void> startFuture) {
        JsonObject config = config();
        String path = config.getString("path_to_config");
        System.out.println("test");

        listService = new ListServiceImpl(getClient());

        Router router = Router.router(vertx);

//        TODO: remove on production
        router.route().handler(CorsHandler.create(".*")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.GET));

//        router.route("/*").handler(StaticHandler.create().setWebRoot("public"));

        router.route().handler(routingContext -> {
            if (tokenInvalid(routingContext)) {
                routingContext.response()
                        .setStatusCode(HttpResponseStatus.UNAUTHORIZED.code())
                        .end();
            } else {
                routingContext.next();
            }
        });

        router.get("/api/list/all").handler(listService::getAllLists);
        router.post("/api/list/add").handler(listService::add);
        router.delete("/api/list/delete").handler(listService::delete);
        router.put("/api/list/update").handler(listService::update);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8090, res -> startServer(startFuture, res));
    }

    private boolean tokenInvalid(RoutingContext routingContext) {
        String authorization = routingContext.request().getHeader("Authorization");
        return authorization == null || invalid(authorization.substring(7));
    }

    private void startServer(Future<Void> startFuture, AsyncResult<HttpServer> res) {
        if (res.succeeded()) {
            startFuture.complete();
        } else {
            startFuture.fail(res.cause());
        }
    }

    private MongoClient getClient() {
        if (client == null) {
            client = MongoClient
                    .createShared(vertx, new JsonObject()
                            .put("db_name", "boards")
                            .put("host", "192.168.88.218")
                    );
        }
        return client;
    }

    private boolean invalid(@Nonnull String token) {
        @Nonnull val now = Instant.now();
        try {
            @Nonnull val claimsJws = parser.setSigningKey(secret).parseClaimsJws(token);
            return ((Long) claimsJws.getBody().get("expiresIn")) < now.toEpochMilli();
        } catch (JwtException e) {
            return true;
        }
    }
}
