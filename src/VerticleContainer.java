package main;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;


public class VerticleContainer extends AbstractVerticle{ //verticle container

   /* public void start(Future<Void> future) {
        Future<String> consumerDeployment = Future.future();
        vertx.deployVerticle(new Consumer(), consumerDeployment.completer());

        Future<String> senderDeployment = Future.future();

        consumerDeployment.compose(id -> {
            vertx.deployVerticle(new Sender(), senderDeployment.completer());
            return senderDeployment;
        });
        senderDeployment.setHandler(res -> {
           if (res.succeeded()) {
               future.complete();
           } else {
               future.fail(res.cause());
           }
        });
    }*/

    public void start() {
        vertx.deployVerticle(new WebServer(), res -> {
            if (res.succeeded()) {
                System.out.println("Successfully deployed HTTP Server.");
            } else {
                System.out.println("Failed to deploy HTTP Server.");
            }
        });

    }

    private void timer() {
        vertx.setTimer(5000, id -> System.out.println("This will be printed in 5 seconds."));
        System.out.println("This is printed");
    }
}