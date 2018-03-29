package main.java;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebServer extends AbstractVerticle {

    //private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private Map<Integer, Dog> dogs = new LinkedHashMap<>();

    @Override
    public void start(Future<Void> future) throws Exception {
        createData(dogs);

        final Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html");
            response.end("From server: Hello world!");
        });

        //String path = new File("").getCanonicalPath();
        router.route("/files/*").handler(StaticHandler.create("assets"));
        router.get("/api/dogs").handler(this::getAll);
        router.route("/api/dogs*").handler(BodyHandler.create());
        router.post("/api/dogs").handler(this::addItem);
        router.delete("/api/dogs/:id").handler(this::deleteItem);

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

    private void createData(Map<Integer, Dog> dogs) {
        Dog dog = new Dog("Beagle", 8);
        dogs.put(dog.getId(), dog);
        dog = new Dog("Golden Retriever ", 12);
        dogs.put(dog.getId(), dog);
    }

    private void addItem(RoutingContext context) {
        final Dog dog = Json.decodeValue(context.getBodyAsString(), Dog.class);
        dogs.put(dog.getId(), dog);
        context.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(dog));
    }

    private void getAll(RoutingContext context) {
        context.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(dogs.values()));
    }

    private void deleteItem(RoutingContext context) {
        final String id = context.request().getParam("id");
        if (id != null) {
            dogs.remove(id);
        }
        context.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(dogs.values()));
    }
}
