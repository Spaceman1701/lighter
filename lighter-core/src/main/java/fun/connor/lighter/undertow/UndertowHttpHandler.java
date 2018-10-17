package fun.connor.lighter.undertow;

import fun.connor.lighter.handler.LighterRequestResolver;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class UndertowHttpHandler implements HttpHandler {

    private LighterRequestResolver resolver;

    public UndertowHttpHandler(LighterRequestResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String url = exchange.getRequestURL();

        Map<String, String> pathParams = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();

        for (Map.Entry<String, Deque<String>> entry : exchange.getPathParameters().entrySet()) {
            pathParams.put(entry.getKey(), entry.getValue().getFirst());
        }

        for (Map.Entry<String, Deque<String>> entry : exchange.getQueryParameters().entrySet()) {
            queryParams.put(entry.getKey(), entry.getValue().getFirst());
        }

        UndertowRequest request = new UndertowRequest(exchange);

        resolver.resolve(pathParams, queryParams, request);
    }
}
