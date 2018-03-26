package main;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class WebServer {
    final private Vertx vertx = Vertx.vertx();

    public void start(Future<Void> future) {
        final Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html");
            response.end("Hello world!");
        });

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(Constants.PORT, res -> {
            if (res.succeeded()) {
                System.out.println("Server listening on port: " + Constants.PORT);
                future.complete();
            } else {
                System.out.println("Failed to start server.");
                future.fail(res.cause());
            }
        });

    }

}
