package fun.connor.lighter.undertow;

import fun.connor.lighter.adapter.TypeAdapter;
import fun.connor.lighter.adapter.TypeAdapterFactory;
import fun.connor.lighter.GlobalRequestTransformer;
import fun.connor.lighter.handler.LighterRequestResolver;
import fun.connor.lighter.http.HttpHeaders;
import fun.connor.lighter.http.MediaType;
import fun.connor.lighter.response.Response;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.PathTemplateMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link HttpHandler} that delegates responsibility to a {@link LighterRequestResolver}.
 * This handler assumes that it is already placed on a worker thread and will usually attempt to perform
 * blocking IO.
 */
public class UndertowHttpHandler implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(UndertowHttpHandler.class);

    private LighterRequestResolver resolver;
    private TypeAdapterFactory adapterFactory;
    private List<GlobalRequestTransformer> requestTransformers;

    /**
     * Construct an HTTP handler that delgeates to the provider {@link LighterRequestResolver}.
     * @param resolver the resolver to delegate to
     * @param typeAdapterFactory the application top level {@link TypeAdapterFactory}.
     * @param requestTransformers the chain of {@link GlobalRequestTransformer} to apply to every
     *                            response.
     */
    UndertowHttpHandler
            (LighterRequestResolver resolver, TypeAdapterFactory typeAdapterFactory,
             List<GlobalRequestTransformer> requestTransformers) {
        this.resolver = resolver;
        this.adapterFactory = typeAdapterFactory;
        this.requestTransformers = requestTransformers;
    }

    /**
     * Parses the path and query parameters and generates a {@link Response} from the
     * {@link LighterRequestResolver}. Then all {@link GlobalRequestTransformer}s are applied
     * to the response and the response is written back to the exchange.
     * @param exchange The exchange to resolve.
     * @throws Exception in the case of an unrecoverable error. This checked exception ensures
     * that no handling error will crash the server.
     */
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
