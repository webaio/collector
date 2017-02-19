package io.weba.collector.web.handler;

import com.hubrick.vertx.kafka.producer.KafkaProducerService;
import com.hubrick.vertx.kafka.producer.model.StringKafkaMessage;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.weba.collector.collector.Collector;

public class CollectorHandlerImpl implements Handler<RoutingContext> {
    private Collector collector;
    private KafkaProducerService kafkaProducerService;

    public CollectorHandlerImpl(Collector collector, KafkaProducerService kafkaProducerService) {
        this.collector = collector;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void handle(RoutingContext context) {
        final HttpServerRequest request = context.request();
        final HttpServerResponse response = request.response();

        kafkaProducerService.sendString(new StringKafkaMessage(collector.collect(context)), result -> {
            if (result.succeeded()) {
                response.setStatusCode(204);
            } else {
                response.setStatusCode(503);
            }

            response.end();
        });
    }
}
