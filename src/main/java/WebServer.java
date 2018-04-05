package main.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WebServer extends AbstractVerticle {

    //private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private Map<Integer, Dog> dogs = new LinkedHashMap<>();
    private MongoClient mongo;
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    //ObjectReader or = new ObjectMapper().reader();

    @Override
    public void start(Future<Void> future) throws Exception {
        final Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html");
            response.end("From server: Hello world!");
        });

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

        JsonObject config = new JsonObject().put("host", "127.0.0.1");
        mongo = MongoClient.createShared(vertx, config);
        Dog dog = new Dog("Beagle", 9);
        createData("dogs", dog);
        dog = new Dog("Golden Retriever ", 12);
        createData("dogs", dog);
    }

    private void createData (final String collection, final Object obj) throws JsonProcessingException {
        String dogToJson = ow.writeValueAsString(obj);
        insertIntoCollection("dogs", dogToJson);
        System.out.println(dogToJson);
    }

    private void insertIntoCollection(final String collectionName, final String jsonObjectAsString) {
        JsonObject json = new JsonObject(jsonObjectAsString);
        mongo.findOne(collectionName, json, null, res -> {
            if (res.succeeded()) {
                if (res.result() != null) {
                    System.out.println("Entry found, will not insert.");
                } else {
                    mongo.insert(collectionName, new JsonObject(jsonObjectAsString),  res1 -> {
                        if (res1.succeeded()) {
                            System.out.println("Successfully inserted entry.");
                        } else {
                            System.out.println("Failed to insert entry.");
                            res1.cause().printStackTrace();
                        }
                    });
                }
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    private void addItem (RoutingContext context) {
        final Dog dog = Json.decodeValue(context.getBodyAsString(), Dog.class);
        try {
            String dogToJson = ow.writeValueAsString(dog);
            insertIntoCollection("dogs", dogToJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        context.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(dog));
    }

    private void getAll(RoutingContext context) {
        final JsonObject jsonObject = new JsonObject();
        mongo.find("dogs", jsonObject, res -> {
            List<JsonObject> objs = res.result();
            context.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(objs));
        });
    }

    private void deleteItem(RoutingContext context) {
        HttpServerRequest req = context.request();
        String str = context.getBodyAsString();
        System.out.println("Request body: " + str);
        final String id = req.getParam("id");
        System.out.println("Id is: " + id);
        if (id == null) {
            context.response().setStatusCode(400).end();
        } else {
            mongo.removeDocument("dogs", new JsonObject().put("_id", id), res -> context.response().setStatusCode(204).end());
        }
    }
}
