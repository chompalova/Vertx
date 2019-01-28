package main.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RESTServer extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(RESTServer.class);
    public static final int PORT = 8080;
    private MongoClient mongo;
    private static final String collectionName = "dogs";

    @Override
    public void start(Future future) throws Exception {
        Router router = Router.router(vertx);
        mongo = MongoClient.createNonShared(vertx, new JsonObject());

        router.get("/api/dogs").handler(this::getAll);
        router.post("/api/dogs").handler(this::addItem);

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).listen(PORT, res -> {
            if (res.succeeded()) {
                System.out.println("Server listening on port: " + PORT);
                future.complete();
            } else {
                System.out.println("Failed to start server.");
                future.fail(res.cause());
            }
        });


    }

    /*private void createData (final String collection, final Object obj) throws JsonProcessingException {
        String dogToJson = ow.writeValueAsString(obj);
        insertIntoCollection("dogs", dogToJson);
        System.out.println("Printing dogs' collection:\n" + dogToJson);
    }*/

    private void insertIntoCollection(final String collectionName, final String jsonObjectAsString) {
        JsonObject json = new JsonObject(jsonObjectAsString);
        mongo.findOne(collectionName, json, null, res -> {
            if (res.succeeded()) {
                if (res.result() != null) {
                    System.out.println("Entry found, will not insert.");
                } else {
                    mongo.insert(collectionName, new JsonObject(jsonObjectAsString),  r -> {
                        if (r.succeeded()) {
                            System.out.println("Successfully inserted entry.");
                        } else {
                            System.out.println("Failed to insert entry.");
                            r.cause().printStackTrace();
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
        final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String dogToJson = ow.writeValueAsString(dog);
            insertIntoCollection(collectionName, dogToJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        context.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(dog));
    }

    private void getAll(RoutingContext context) {
        JsonObject jsonObject = new JsonObject();
        mongo.find("dogs", jsonObject, res -> {
            if (res.succeeded()) {
                context.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(res.result()));
            } else {
                res.cause().printStackTrace();
                logger.debug("Failed to get Mongo records.");
            }
        });
    }
}
