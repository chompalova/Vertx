package main.java;

import io.vertx.core.Vertx;

public class ServiceLauncher {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(VerticleContainer.class.getName());
    }
}
