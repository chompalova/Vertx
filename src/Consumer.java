import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import org.jruby.RubyProcess;

import java.sql.Timestamp;

public class Consumer extends AbstractVerticle{
    protected EventBus eventBus = null;
    final public static String NAME = "Consumer";

    @Override
    public void start() {
        eventBus = vertx.eventBus();
        //eventBus.consumer(Constants.ADDRESS, msg -> System.out.println(new Timestamp(System.currentTimeMillis()) + ": " + "A message received: " + msg.body()));
        eventBus.consumer(Constants.ADDRESS, msg -> {
            System.out.println("A message received: " + msg.body());
            /*msg.reply("Hi from consumer." , res -> {
                if (res.succeeded()) {
                    System.out.println("Consumer: Successfully sent reply");
                } else {
                    System.out.println("Consumer: Failed to send reply." + res.cause());
                }
            });*/
            msg.reply("ACK from Consumer.");
        });
    }
}
