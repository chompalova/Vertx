package main.java;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

public class HttpServerVerticle extends AbstractVerticle{
    private HttpServer httpServer = null;

    @Override
    public void start(Future <Void> future) {
        httpServer = vertx.createHttpServer();
        httpServer.requestHandler(request -> handleHttpRequest(request));
        httpServer.listen(Constants.PORT, res -> {
            if (res.succeeded()) {
                System.out.println("Server listening on port: " + Constants.PORT);
                future.complete();
            } else {
                System.out.println("Failed to start server.");
                future.fail(res.cause());
            }
        });
    }

    private void handleHttpRequest(final HttpServerRequest httpRequest) {
        /*vertx.eventBus().send(Constants.ADDRESS, "Hello world", res -> {
            if (res.succeeded()) {
                httpRequest.response().end(res.result().body().toString());
                System.out.println("From server: sent a response.");
            } else {
                httpRequest.response().setStatusCode(500).end(res.cause().getMessage());
                System.out.println("From server: failed to handle request.");
            }
        });*/
        httpRequest.response().end( "Hello world!");
    }

    @Override
    public void stop() {
        System.out.println("Vehicle1 stopping");
    }
}
