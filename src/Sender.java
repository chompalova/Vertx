import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Sender extends AbstractVerticle {
    final public static String NAME = "Sender";

    @Override
    public void start() {
        EventBus eventBus = vertx.eventBus();
        //vertx.setPeriodic(5000, res -> eventBus.publish(Constants.ADDRESS,"From sender: flooding the network."));
        eventBus.send(Constants.ADDRESS, "Hello from sender", res -> {
            if (res.succeeded()) {
                System.out.println("Sender: Received a reply: " + res.result().body());
            } else {
                System.out.println("Sender: Failed to receive a reply." + res.cause());
            }
        });
    }

    @Override
    public void stop() {
        System.out.println("Sender verticle stopping");
    }
}
