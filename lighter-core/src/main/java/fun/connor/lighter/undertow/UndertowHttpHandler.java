package fun.connor.lighter.undertow;

import com.google.gson.Gson;
import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.handler.Response;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.PathTemplateMatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UndertowHttpHandler implements HttpHandler {

    private LighterRequestResolver resolver;
    private TypeAdapterFactory adapterFactory;

    public UndertowHttpHandler(LighterRequestResolver resolver, TypeAdapterFactory typeAdapterFactory) {
        this.resolver = resolver;
        this.adapterFactory = typeAdapterFactory;
    }

    @Override @SuppressWarnings("unchecked")
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Map<String, String> pathParams = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();

        PathTemplateMatch pathMatch = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);
        for (Map.Entry<String, String> entry : pathMatch.getParameters().entrySet()) {
            System.out.println(entry.getKey());
            pathParams.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Deque<String>> entry : exchange.getQueryParameters().entrySet()) {
            queryParams.put(entry.getKey(), entry.getValue().getFirst());
        }

        UndertowRequest request = new UndertowRequest(exchange);

        Response r = resolver.resolve(pathParams, queryParams, request);

        exchange.setStatusCode(r.getStatus());
        Map<String, String> customHeaders = r.getCustomHeaders();
        for (Map.Entry<String, String> header : customHeaders.entrySet()) {
            exchange.getResponseHeaders().put(HttpString.tryFromString(header.getKey()), header.getValue());
        }
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        if (r.hasContent()) {
            TypeAdapter typeAdapter = adapterFactory.getAdapter(r.getContent().getClass());
            exchange.getResponseSender().send(typeAdapter.serialize(r.getContent()));
        }
    }
}
