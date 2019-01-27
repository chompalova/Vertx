package main.java;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class ServiceLauncher {

    private static final Logger logger = LoggerFactory.getLogger(RESTServer.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(VerticleContainer.class.getName());
        System.out.println("Successfully deployed verticle container.");
    }
}
