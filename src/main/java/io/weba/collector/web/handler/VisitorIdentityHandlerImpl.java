package io.weba.collector.web.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

public class VisitorIdentityHandlerImpl implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext context) {
        UUID uuid;
        try {
            Cookie vid = context.getCookie("vid");
            uuid = UUID.fromString(Objects.isNull(vid) ? "" : vid.getValue());
        } catch (IllegalArgumentException ex) {
            uuid = UUID.randomUUID();
        }

        Cookie cookie = Cookie.cookie("vid", uuid.toString())
                .setHttpOnly(true)
                .setSecure(false)
                .setMaxAge(Duration.ofDays(365).getSeconds());

        context.addCookie(cookie);
        context.next();
    }
}
