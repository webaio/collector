package io.weba.collector.collector;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

public class Collector {
    public String collect(RoutingContext ctx) {
        JsonObject map = new JsonObject();
        map.put("request", collectRequest(ctx));
        map.put("response", collectResponse(ctx));

        System.out.println(map);
        return map.encode();
    }

    private JsonObject collectResponse(RoutingContext ctx) {
        JsonObject response = new JsonObject();
        response.put("status", ctx.response().getStatusCode());
        response.put("headers", ctx.response().headers().entries());
        return response;
    }

    private JsonObject collectRequest(RoutingContext ctx) {
        JsonObject request = new JsonObject();
        request.put("id", UUID.randomUUID().toString());
        request.put("method", ctx.request().rawMethod());
        request.put("uri", ctx.request().absoluteURI());
        request.put("headers", ctx.request().headers().entries());
        request.put("content", ctx.getBodyAsString());
        request.put("cookies", ctx.cookies()
                .stream()
                .map(cookie -> String.format("%s:%s", cookie.getName(), cookie.getValue()))
                .collect(Collectors.toList())
        );

        TimeZone timezone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        dateFormat.setTimeZone(timezone);

        request.put("date", dateFormat.format(new Date()));
        request.put("remote_addr", ctx.request().remoteAddress().host());
        return request;
    }
}
