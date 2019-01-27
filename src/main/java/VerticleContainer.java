package main.java;

import io.vertx.core.AbstractVerticle;


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
   @Override
    public void start() {
        vertx.deployVerticle(new RESTServer(), res -> {
            if (res.succeeded()) {
                System.out.println("VerticleContainer -> Successfully deployed RESTServer");
            } else {
                System.out.println("VerticleContainer -> Failed to deploy RESTServer.");
                res.cause().printStackTrace();
            }
        });

    }

    private void timer() {
        vertx.setTimer(5000, id -> System.out.println("This will be printed in 5 seconds."));
        System.out.println("This is printed");
    }
}