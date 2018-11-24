package fun.connor.lighter.undertow;

import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.global.GlobalRequestTransformer;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.http.HttpHeaders;
import fun.connor.lighter.http.MediaType;
import fun.connor.lighter.response.Response;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.PathTemplateMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UndertowHttpHandler implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(UndertowHttpHandler.class);

    private LighterRequestResolver resolver;
    private TypeAdapterFactory adapterFactory;
    private List<GlobalRequestTransformer> requestTransformers;

    public UndertowHttpHandler
            (LighterRequestResolver resolver, TypeAdapterFactory typeAdapterFactory,
             List<GlobalRequestTransformer> requestTransformers) {
        this.resolver = resolver;
        this.adapterFactory = typeAdapterFactory;
        this.requestTransformers = requestTransformers;
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

        Response response = resolver.resolve(pathParams, queryParams, request);

        for (GlobalRequestTransformer transformer : requestTransformers) {
            response = transformer.apply(request, response);
        }

        exchange.setStatusCode(response.getStatus());
            Map<String, String> customHeaders = response.getHeaders();
        for (Map.Entry<String, String> header : customHeaders.entrySet()) {
            exchange.getResponseHeaders().put(HttpString.tryFromString(header.getKey()), header.getValue());
        }

        if (response.hasContent()) {
            String contentType = getContentType(response);
            TypeAdapter typeAdapter = adapterFactory.getAdapter(response.getContent().getClass(), contentType);
            exchange.getResponseSender().send(typeAdapter.serialize(response.getContent()));
        }
    }

    private String getContentType(Response<?> response) {
        String contentType = response.getHeaders().get(HttpHeaders.CONTENT_TYPE);
        if (contentType == null) {
            contentType = MediaType.TEXT_PLAIN;
        }
        return contentType;
    }
}
