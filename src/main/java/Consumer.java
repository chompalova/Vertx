package main.java;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Consumer extends AbstractVerticle{
    final public static String NAME = "CONSUMER";

    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();
        eventBus.consumer(Constants.ADDRESS, msg -> {
            System.out.println("Consumer: a message received: " + msg.body());
            msg.reply("ACK from Consumer.");
        });
    }
}
