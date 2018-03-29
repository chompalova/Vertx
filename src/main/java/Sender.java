package main.java;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Sender extends AbstractVerticle {
    final public static String NAME = "SENDER";

    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();
        eventBus.send(Constants.ADDRESS, "Hello from sender.", res -> {
            if (res.succeeded()) {
                System.out.println("Sender: received a reply: " + res.result().body());
            } else {
                System.out.println("Sender: failed to receive a reply." + res.cause());
            }
        });
    }

    @Override
    public void stop() {
        System.out.println("Sender verticle stopping");
    }
}
