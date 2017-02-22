package io.weba.collector.verticle;

import com.hubrick.vertx.kafka.producer.KafkaProducerService;
import com.hubrick.vertx.kafka.producer.model.StringKafkaMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.weba.collector.Constants;
import io.weba.collector.collector.Collector;
import io.weba.collector.web.handler.CollectorHandler;
import io.weba.collector.web.handler.VisitorIdentityHandler;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

public class TrackerVerticle extends AbstractVerticle {
    private KafkaProducerService kafkaProducerService;
    private HttpServer server;
    private Router router;
    private Collector collector = new Collector();

    public static void main(String[] args) {
        System.out.print("This is vert.x application. You can run it from CLI. See more information in README.md.");
    }

    @Override
    public void start(Future<Void> future) throws Exception {
        kafkaProducerService = KafkaProducerService.createProxy(
                vertx,
                config().getJsonObject(Constants.IO_WEBA_COLLECTOR_KAFKA_PRODUCER).getString("address")
        );

        server = vertx.createHttpServer();
        router = createRouter();

        server.requestHandler(router::accept);
        server.listen(
                config().getInteger(Constants.IO_WEBA_COLLECTOR_TRACKER_HTTP_PORT, 8080),
                (AsyncResult<HttpServer> result) -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                }
        );
    }

    private Router createRouter() {
        router = Router.router(vertx);
        router.route().handler(CookieHandler.create());
        router.route().handler(VisitorIdentityHandler.create());

        // collect route
        Route collectRoute = router.route(HttpMethod.GET, "/collect");
        collectRoute.handler(BodyHandler.create());
        collectRoute.handler(CollectorHandler.create(collector, kafkaProducerService));

        return router;
    }
}
