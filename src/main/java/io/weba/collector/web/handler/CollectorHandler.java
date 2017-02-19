package io.weba.collector.web.handler;

import com.hubrick.vertx.kafka.producer.KafkaProducerService;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.weba.collector.collector.Collector;

@VertxGen
public interface CollectorHandler extends Handler<RoutingContext> {
    static CollectorHandlerImpl create(Collector collector, KafkaProducerService kafkaProducerService) {
        return new CollectorHandlerImpl(collector, kafkaProducerService);
    }
}
