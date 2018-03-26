import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import org.jruby.RubyProcess;

import java.sql.Timestamp;

public class Consumer extends AbstractVerticle{
    final public static String NAME = "CONSUMER";

    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();
        eventBus.consumer(Constants.ADDRESS, msg -> {
            System.out.println("A message received: " + msg.body());
            msg.reply("ACK from Consumer.");
        });
    }
}
