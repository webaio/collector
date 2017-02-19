package io.weba.collector.web.handler;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

@VertxGen
public interface VisitorIdentityHandler extends Handler<RoutingContext> {
    static VisitorIdentityHandlerImpl create() {
        return new VisitorIdentityHandlerImpl();
    }
}
