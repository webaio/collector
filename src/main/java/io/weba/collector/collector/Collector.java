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
    public String collect(RoutingContext context) {
        JsonObject map = new JsonObject();
        map.put("request", collectRequest(context));
        map.put("response", collectResponse(context));

        return map.encode();
    }

    private JsonObject collectResponse(RoutingContext context) {
        JsonObject response = new JsonObject();
        response.put("status", context.response().getStatusCode());
        response.put("headers", context.response().headers().entries());

        return response;
    }

    private JsonObject collectRequest(RoutingContext context) {
        JsonObject request = new JsonObject();
        request.put("id", UUID.randomUUID().toString());
        request.put("method", context.request().rawMethod());
        request.put("uri", context.request().absoluteURI());
        request.put("headers", context.request().headers().entries());
        request.put("content", context.getBodyAsString());
        request.put("cookies", context.cookies()
                .stream()
                .map(cookie -> String.format("%s:%s", cookie.getName(), cookie.getValue()))
                .collect(Collectors.toList())
        );

        TimeZone timezone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        dateFormat.setTimeZone(timezone);

        request.put("date", dateFormat.format(new Date()));
        request.put("remote_addr", context.request().remoteAddress().host());

        return request;
    }
}
