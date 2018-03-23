import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

public class HttpServerVerticle extends AbstractVerticle{
    private HttpServer httpServer = null;
    final public static int PORT = 9999;

    @Override
    public void start(Future <Void> future) {
        httpServer = vertx.createHttpServer();
        httpServer.requestHandler(request -> handleHttpRequest(request));
        httpServer.listen(PORT, "192.168.0.104", res -> {
            if (res.succeeded()) {
                System.out.println("Server listening on port: " + PORT);
                future.complete();
            } else {
                System.out.println("Failed to start server.");
                future.fail(res.cause());
            }
        });
    }

    private void handleHttpRequest(final HttpServerRequest httpRequest) {
        vertx.eventBus().send(Constants.ADDRESS, "Hello world", res -> {
            if (res.succeeded()) {
                httpRequest.response().end(res.result().body().toString());
                System.out.println("Request successfully handled.");
            } else {
                httpRequest.response().setStatusCode(500).end(res.cause().getMessage());
                System.out.println("From server: failed to handle request.");
            }
        });
    }

    @Override
    public void stop() {
        System.out.println("Vehicle1 stopping");
    }
}
