package io.weba.collector.verticle;

import io.vertx.core.*;
import io.weba.collector.Constants;

public class DeploymentVerticle extends AbstractVerticle {
    public static void main(String[] args) {
        System.out.print("This is vert.x application. You can run it from CLI. See more information in README.md.");
    }

    @Override
    public void start(Future<Void> future) throws Exception {
        Vertx vertx = Vertx.vertx();

        deployKafkaVerticle(vertx);
        deployTrackerVerticle(vertx);
    }

    private void deployTrackerVerticle(Vertx vertx) {
        String verticleName = config().getString(Constants.IO_WEBA_COLLECTOR_TRACKER_VERTICLE_CLASS, Constants.TRACKER_PRODUCER_VERTICLE);

        vertx.deployVerticle(
                verticleName,
                new DeploymentOptions().setConfig(config()),
                deploymentHandler(verticleName)
        );
    }

    private void deployKafkaVerticle(Vertx vertx) {
        DeploymentOptions kafkaDeploymentOptions = new DeploymentOptions()
                .setWorker(true)
                .setConfig(config().getJsonObject(Constants.IO_WEBA_COLLECTOR_KAFKA_PRODUCER))
                .setInstances(config().getInteger(Constants.IO_WEBA_COLLECTOR_KAFKA_VERTICLE_WORKERS, 2));

        String verticleName = config().getString(Constants.IO_WEBA_COLLECTOR_KAFKA_VERTICLE_CLASS, Constants.KAFKA_PRODUCER_VERTICLE);

        vertx.deployVerticle(
                verticleName,
                kafkaDeploymentOptions,
                deploymentHandler(verticleName)
        );
    }

    private Handler<AsyncResult<String>> deploymentHandler(String verticleName) {
        return response -> {
            if (response.succeeded()) {
                String deploymentID = response.result();
                System.out.println(String.format(
                        "%s verticle deployed, deploymentID = %s",
                        verticleName,
                        deploymentID
                ));
            } else {
                response.cause().printStackTrace();
            }
        };
    }
}
