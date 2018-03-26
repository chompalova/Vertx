import io.vertx.core.Vertx;
import javaslang.collection.List;

public class ServiceLauncher { //verticle container

        private static Vertx vertx = Vertx.vertx();

        public static void main(String[] args) {
            //JsonObject config = new JsonObject().put("name", "tim").put("directory", "/test");
            //DeploymentOptions options = new DeploymentOptions().setConfig(config);
            vertx.deployVerticle(new Consumer(), res -> {
                if (res.succeeded()) {
                    System.out.println("Verticle " + Consumer.NAME + " deployed.");
                    vertx.deployVerticle(new Sender());
                    System.out.println("Verticle " + Sender.NAME + " deployed.");
                } else {
                    System.out.println("Verticle " + Consumer.NAME + " not deployed.");
                }
            });
        }

    public static void timer() {
        vertx.setTimer(5000, id -> System.out.println("This will be printed in 5 seconds."));
        System.out.println("This is printed");
    }
}