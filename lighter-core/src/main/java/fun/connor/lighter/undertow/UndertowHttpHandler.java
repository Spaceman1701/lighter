package fun.connor.lighter.undertow;

import com.google.gson.Gson;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.handler.Response;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.PathTemplateMatch;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class UndertowHttpHandler implements HttpHandler {

    private LighterRequestResolver resolver;

    public UndertowHttpHandler(LighterRequestResolver resolver) {
        this.resolver = resolver;
    }

    @Override @SuppressWarnings("unchecked")
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String url = exchange.getRequestURL();

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
        Gson gson = new Gson();
        exchange.getResponseSender().send(gson.toJson(r.getContent()));
    }
}
